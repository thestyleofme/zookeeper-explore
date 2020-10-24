package com.github.thestyleofme.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/23 22:55
 * @since 1.0.0
 */
public class CreateNode {

    public static void main(String[] args) throws Exception {
        RetryPolicy backoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(backoffRetry)
                // 设置独立的命名空间 /base 所有节点都是/base开头的，数据隔离
                .namespace("base")
                .build();
        client.start();
        System.out.println("会话创建了");
        String forPath = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath("/test-curator/c1", "123".getBytes());
        System.out.println("节点递归创建成功，该节点路径" + forPath);
        client.close();
    }
}
