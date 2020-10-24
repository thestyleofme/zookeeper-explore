package com.github.thestyleofme.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/23 22:55
 * @since 1.0.0
 */
public class UpdateNode {

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
        String path = "/test-curator/c1";
        System.out.println("会话创建了");

        // 数据内容以及状态信息
        Stat stat = new Stat();
        byte[] bytes1 = client.getData().storingStatIn(stat).forPath(path);

        System.out.println("获取到的节点，数据内容：" + new String(bytes1) +
                "，状态信息：" + stat);

        // 更新节点内容
        int version = client.setData()
                .withVersion(stat.getVersion())
                .forPath(path, "修改内容1".getBytes())
                .getVersion();
        System.out.println("当前的最新版本是" + version);
        byte[] bytes2 = client.getData().forPath(path);
        System.out.println("修改后的节点数据内容：" + new String(bytes2));

        // BadVersionException
        client.setData()
                .withVersion(stat.getVersion())
                .forPath(path, "修改内容2".getBytes());

        client.close();
    }
}
