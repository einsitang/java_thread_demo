# README

知乎 某回答引发出来争议，此处贴上参考的执行代码

# 环境
jdk 1.8
MacOS (i7 16G)

# 依赖
- httpclient
- vertx-web-client

# 结果样本
```
pressure = 1500;
线程爬虫
...
task [MT_48] job run time : 15127 ms
task [MT_23] job run time : 15418 ms
task [MT_35] job run time : 15297 ms
task [MT_12] job run time : 14661 ms
task [MT_59] job run time : 15260 ms
task [MT_7] job run time : 14807 ms
task [MT_75] job run time : 15691 ms
done time : 23219 ms

事件轮训爬虫
...
done time: 1571033947213
task [EL_84] job run time : 32 ms
done time: 1571033947342
task [EL_85] job run time : 31 ms
done time: 1571033947381
task [EL_86] job run time : 31 ms
done time: 1571033947434
task [EL_87] job run time : 31 ms
done time: 1571033947468
task [EL_88] job run time : 31 ms
done time: 1571033947535
task [EL_89] job run time : 31 ms
done time: 1571033947735
done time : 8114 ms
```