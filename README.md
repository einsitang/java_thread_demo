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

# changelog
```
更新EL(eventloop)和MT(multiThreading)统一使用vertxClient作为Client

使用相同的vertxClient作为Client 各自取最优的 样本结果 ：
pressure = 0;
taskCount = 90
MT
8.7s
EL
6.8s
【当前样本无意义，MT更换VertxClient那一刻它就是事件轮训机制，只能证明多出的时间是无效线程的创建和销毁的损耗】
```
```
移除 MT 使用vertxClient 作为 Client 的方式，因为一旦这样做已经就是事件轮训了，new Thread是无用功

```