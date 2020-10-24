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
 * @author isaac 2020/10/23 0:44
 * @since 1.0.0
 */
public class UpdateNodeData implements Watcher {

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
                5000, new UpdateNodeData());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 回调方法：处理来自服务器端的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            updateDataNodeSync();
        }
    }

    private void updateDataNodeSync() {
        /*
            path:路径
            data:要修改的内容 byte[]
            version:为-1，表示对最新版本的数据进行修改
            zooKeeper.setData(path, data,version)
         */
        try {
            byte[] data = zooKeeper.getData("/test-persistent", false, null);
            System.out.println("修改前的值：" + new String(data));

            Stat stat = zooKeeper.setData("/test-persistent", "456".getBytes(), -1);
            System.out.println("stat: " + stat);

            byte[] data2 = zooKeeper.getData("/test-persistent", false, null);
            System.out.println("修改后的值：" + new String(data2));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

}
