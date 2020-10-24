package com.github.thestyleofme.zk.api;

import java.io.IOException;

import org.apache.zookeeper.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/22 1:00
 * @since 1.0.0
 */
public class CreateNode implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
        客户端可以通过创建一个zk实例来连接zk服务器
        new Zookeeper(connectString,sessionTimeOut,Watcher)
        connectString: 连接地址：IP：端口
        sessionTimeOut：会话超时时间：单位毫秒
        Watcher：监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
         */
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000, new CreateNode());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 回调方法：处理来自服务器端的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            try {
                createNodeSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private static void createNodeSync() throws KeeperException, InterruptedException {
        /*
         *  path        ：节点创建的路径
         *  data[]      ：节点创建要保存的数据，是个byte类型的
         *  acl         ：节点创建的权限信息(4种类型)
         *                 ANYONE_ID_UNSAFE    : 表示任何人
         *                 AUTH_IDS    ：此ID仅可用于设置ACL。它将被客户机验证的ID替换。
         *                 OPEN_ACL_UNSAFE    ：这是一个完全开放的ACL(常用)--> world:anyone
         *                 CREATOR_ALL_ACL  ：此ACL授予创建者身份验证ID的所有权限
         *  createMode    ：创建节点的类型(4种类型)
         *                  PERSISTENT：持久节点
         *				    PERSISTENT_SEQUENTIAL：持久顺序节点
         *                  EPHEMERAL：临时节点
         *                  EPHEMERAL_SEQUENTIAL：临时顺序节点
         * String node = zookeeper.create(path,data,acl,createMode)
         */
        String nodePersistent = zooKeeper.create("/test-persistent", "持久节点内容".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("创建的持久节点: " + nodePersistent);
        String nodePersistentSequential = zooKeeper.create("/test-persistent_sequential", "持久顺序节点内容".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("创建的持久顺序节点: " + nodePersistentSequential);
        String nodeEphemeral = zooKeeper.create("/test-ephemeral", "临时节点内容".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建的临时节点: " + nodeEphemeral);
    }
}
