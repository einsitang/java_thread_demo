package com.demo;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;


public class Main {

    private static long programBeginTime = System.currentTimeMillis();
    private static int taskCount = 90;
    private static int taskDoneCount = 0;

    static final HttpClient client = HttpClients.createDefault();

    static String HOST = "www.zhihu.com";
    static String URI = "/question/287421003/answer/814015100";
    static int PORT = 443;

    /**
     * 模拟CPU密集计算
     */
    static int pressure = 1500;

    public static void timing() {
        taskDoneCount++;
        if (taskDoneCount >= taskCount) {
            long programEndTime = System.currentTimeMillis();
            System.out.println(String.format("done time : %d ms", programEndTime - programBeginTime));
        }
    }

    public static void main(String[] args) {

//        justNewThreadYouWant();

        // 单纯的线程的爬虫
//        multiThreading();

        // 事件轮训线程复用的爬虫
        eventLoop();
    }

    /**
     * 你喜欢创建多少个线程都可以，都在wating，有意义吗？
     */
    private static void justNewThreadYouWant(){
        int threadCount = 9000;
        Thread thread;
        for(int i=0;i<threadCount;++i){
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            thread.start();;
        }
    }

    private static void multiThreading() {
        Thread task;

        final String URL = "https://" + HOST + URI;
        for (int i = 0; i < taskCount; ++i) {

            final String taskName = "MT_" + i;

            task = new Thread(new Runnable() {
                @Override
                public void run() {
//                    long beginTime = System.currentTimeMillis();
                    HttpGet httpGet = new HttpGet(URL);
                    org.apache.http.HttpResponse resp;
                    String body = null;
                    try {

                        resp = client.execute(httpGet);
                        if (resp.getStatusLine().getStatusCode() == 200) {
                            body = EntityUtils.toString(resp.getEntity(), "UTF-8");
                        }

                    } catch (IOException e) {
                        System.err.println("error : " + e.getMessage());
                        // retry
                    }
                    JobUtils.dosomeThing(taskName, body, pressure);
//                    long endTime = System.currentTimeMillis();
//                    System.out.println(String.format("task [%s] run time : %d ms", taskName, endTime - beginTime));
//                    System.out.println(String.format("task [%s] timestamp : %d", taskName, System.currentTimeMillis()));
                    timing();
                }
            });
            task.start();
        }
    }

    private static void eventLoop() {

        VertxOptions vertxOptions = new VertxOptions().setAddressResolverOptions(
                new AddressResolverOptions().setSearchDomains(Collections.emptyList())
        );
        Vertx vertx = Vertx.vertx(vertxOptions);

        SelfSignedCertificate certificate = SelfSignedCertificate.create();

        WebClientOptions webClientoptions = new WebClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setKeyCertOptions(certificate.keyCertOptions())
                .setTrustOptions(certificate.trustOptions());
        WebClient client = WebClient.create(vertx, webClientoptions);

        for (int i = 0; i < taskCount; ++i) {

            final String taskName = "EL_" + i;
            RequestOptions requestOptions = new RequestOptions()
                    .setSsl(true)
                    .setPort(PORT)
                    .setHost(HOST)
                    .setURI(URI);
            client.request(HttpMethod.GET, requestOptions)
                    .send(ar -> {

                        if (!ar.succeeded()) {
                            System.err.println("error : " + ar.cause().getMessage());
                            // retry
                        }else{
                            HttpResponse<Buffer> response = ar.result();
                            if (response.statusCode() == 200) {
                                String body = response.bodyAsString();
                                JobUtils.dosomeThing(taskName, body, pressure);
                                System.out.println(String.format("done time: %d", System.currentTimeMillis()));
                            }
                        }
                        timing();
                    });
        }


    }

}
