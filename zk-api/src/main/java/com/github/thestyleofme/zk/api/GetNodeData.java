package com.github.thestyleofme.zk.api;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/23 0:15
 * @since 1.0.0
 */
public class GetNodeData implements Watcher {

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
                5000, new GetNodeData());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 回调方法：处理来自服务器端的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        /*
            子节点列表发生改变时，服务器端会发生nodeChildrenChanged事件通知
            要重新获取子节点列表，同时注意：通知是一次性的，需要反复注册监听
         */
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            try {
                List<String> children = zooKeeper.getChildren("/test-persistent", true);
                System.out.println(children);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            // 获取节点数据
            try {
                getNodeData();
                getChildren();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void getNodeData() throws KeeperException, InterruptedException {
        /*
          path    : 获取数据的路径
          watch    : 是否开启监听
          stat    : 节点状态信息
          stat为null: 表示获取最新版本的数据
          zk.getData(path, watch, stat)
         */
        byte[] data = zooKeeper.getData("/test-persistent", false, null);
        System.out.println(new String(data));
    }

    private static void getChildren() throws KeeperException, InterruptedException {
         /*
            path:路径
            watch:是否要启动监听，当子节点列表发生变化，会触发监听
            zooKeeper.getChildren(path, watch)
         */
        List<String> children = zooKeeper.getChildren("/test-persistent", true);
        System.out.println(children);
    }
}
