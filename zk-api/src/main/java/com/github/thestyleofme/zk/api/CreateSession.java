package com.github.thestyleofme.zk.api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/22 1:00
 * @since 1.0.0
 */
public class CreateSession implements Watcher {

    /**
     * CountDownLatch主要有两个方法，当一个或多个线程调用await方法时，这些线程会阻塞
     * 其他线程调用countDown方法时会将计数器减1（调用countDown方法的线程不会阻塞）
     * 当计数器的值变为0时，因await方法阻塞的线程会被唤醒，继续执行
     */
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
        客户端可以通过创建一个zk实例来连接zk服务器
        new Zookeeper(connectString,sessionTimeOut,Watcher)
        connectString: 连接地址：IP：端口
        sessionTimeOut：会话超时时间：单位毫秒
        Watcher：监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
         */
        try (ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000, new CreateSession())) {
            System.out.println(zooKeeper.getState());

            // 计数工具类：CountDownLatch:不让main方法结束，让线程处于等待阻塞
            COUNT_DOWN_LATCH.await();

            System.out.println("客户端与服务端会话真正建立了");
        }
    }

    /**
     * 回调方法：处理来自服务器端的watcher通知
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        // SyncConnected
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            // 解除主程序在CountDownLatch上的等待阻塞
            System.out.println("process方法执行了...");
            COUNT_DOWN_LATCH.countDown();
        }
    }
}
