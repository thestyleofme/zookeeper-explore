package com.github.thestyleofme.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/23 3:08
 * @since 1.0.0
 */
public class CreateSession {

    public static void main(String[] args) {
        RetryPolicy backoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client1 =
                CuratorFrameworkFactory.newClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", backoffRetry);
        client1.start();
        System.out.println("会话1被创建了");

        // 推荐如下方式
        CuratorFramework client2 = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(backoffRetry)
                // 设置独立的命名空间 /base 所有节点都是/base开头的，数据隔离
                .namespace("base")
                .build();
        client2.start();
        System.out.println("会话2被创建了");

        client1.close();
        client2.close();
    }
}
