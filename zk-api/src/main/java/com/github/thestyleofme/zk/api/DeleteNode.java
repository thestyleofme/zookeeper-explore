package com.github.thestyleofme.zk.api;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/23 1:00
 * @since 1.0.0
 */
public class DeleteNode implements Watcher {

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
                5000, new DeleteNode());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 回调方法：处理来自服务器端的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            deleteNodeSync();
        }
    }

    private void deleteNodeSync() {
         /*
            zooKeeper.exists(path,watch) :判断节点是否存在
            zookeeper.delete(path,version) : 删除节点
        */
        Stat stat;
        try {
            stat = zooKeeper.exists("/test-persistent/c1", false);
            System.out.println(stat == null ? "该节点不存在" : "该节点存在");

            if (stat != null) {
                zooKeeper.delete("/test-persistent/c1", -1);
            }

            stat = zooKeeper.exists("/test-persistent/c1", false);
            System.out.println(stat == null ? "该节点不存在" : "该节点存在");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

    }

}
