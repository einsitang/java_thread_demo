package com.demo;

public class JobUtils {

    /**
     *
     * @param taskName
     * @param body
     * @param pressure  压力，数字越多，耗时越高，模拟消耗CPU操作
     */
    public static void dosomeThing(String taskName, String body,int pressure) {

//        System.out.println(String.format("task : %s -> body : %s", taskName, body));
        if (body == null) {
            // req fail
            return;
        }

        long begin = System.currentTimeMillis();
        int loopCount = pressure;

        for (int i = 0; i < loopCount; ++i) {
            int j = i;
            while (--j > 0) {
                //
                double c = Math.random() + i + j;
            }
        }
        // DOM parse
        // data extract
        // store
        // done
        long end = System.currentTimeMillis();
        System.out.println(String.format("task [%s] job run time : %d ms", taskName, end - begin));
    }

}
