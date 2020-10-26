## zookeeper-explore

### zk-api

原生api以及curator方式对zk的操作

### zk-config-web-demo

zk统一配置mysql数据源配置的一个web demo

### zk应用场景

- 数据发布订阅
    * 统一配置管理
- 命名服务
    * 分布式id
- 集群管理
    * 例如分布式日志收集系统
- Master选举
- 分布式锁
    * 排它锁（X锁）
    * 共享锁（S锁）
        * 羊群效应
- 分布式队列
    * FIFO
    * Barrier（分布式屏障）

### zk深入进阶

- ZAB协议
    * 崩溃恢复
    * 消息广播
    * ZAB与Paxos的联系与区别
- 服务器角色
    * Leader
    * Follower
    * Observer
- 服务器启动
    * 单机
    * 集群
- Leader选举
    * 服务器启动时期的Leader选举
    * 服务器运行时期的Leader选举

### zk源码剖析

- 单机模式服务端启动
- FastLeaderElection选举策略
- 集群模式服务端启动