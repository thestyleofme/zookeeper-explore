package com.github.thestyleofme.demo.utils;

import com.github.thestyleofme.demo.pojo.MysqlDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/26 15:19
 * @since 1.0.0
 */
@Slf4j
public class DatasourceProvider {

    private DatasourceProvider() {

    }

    private static final String NODE_PATH = "/web-app/mysql-config";

    private static HikariDataSource hikariDataSource;
    private static final CuratorFramework CLIENT;

    static {
        RetryPolicy backoffRetry = new ExponentialBackoffRetry(1000, 3);
        CLIENT = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(backoffRetry)
                // 设置独立的命名空间 /base 所有节点都是/base开头的，数据隔离
                .namespace("base")
                .build();
        CLIENT.start();
        // 创建一个默认节点 默认mysql配置
        MysqlDatasource mysqlDatasource = genDefaultMysqlDatasource();
        String data = JsonUtil.toJson(mysqlDatasource);
        try {
            CLIENT.create()
                    .orSetData()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(NODE_PATH, data.getBytes());
        } catch (Exception e) {
            log.error("error", e);
        }
        // 设置监听
        CuratorCache cache = CuratorCache.build(CLIENT, NODE_PATH);
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forChanges(DatasourceProvider::doNodeChange)
                .build();
        cache.listenable().addListener(listener);
        cache.start();
        // 生成HikariDataSource
        hikariDataSource = genHikariDataSource(mysqlDatasource);
    }

    public static HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    private static void doNodeChange(ChildData oldNode, ChildData node) {
        try {
            // 重新生成HikariDataSource
            String data = new String(CLIENT.getData().forPath(NODE_PATH));
            MysqlDatasource mysqlDatasource = JsonUtil.toObj(data, MysqlDatasource.class);
            hikariDataSource = genHikariDataSource(mysqlDatasource);
            log.debug("update HikariDataSource, {}", hikariDataSource.toString());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static HikariDataSource genHikariDataSource(MysqlDatasource mysqlDatasource) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mysqlDatasource.getUrl());
        hikariConfig.setUsername(mysqlDatasource.getUsername());
        hikariConfig.setPassword(mysqlDatasource.getPassword());
        hikariConfig.setDriverClassName(mysqlDatasource.getDriver());
        hikariConfig.setSchema(mysqlDatasource.getDefaultSchema());
        return new HikariDataSource(hikariConfig);
    }

    private static MysqlDatasource genDefaultMysqlDatasource() {
        MysqlDatasource mysqlDatasource = new MysqlDatasource();
        mysqlDatasource.setUrl("jdbc:mysql://127.0.0.1:3306/cus_spring?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        mysqlDatasource.setUsername("root");
        mysqlDatasource.setPassword("tse@9527");
        mysqlDatasource.setDriver("com.mysql.jdbc.Driver");
        mysqlDatasource.setDefaultSchema("cus_spring");
        return mysqlDatasource;
    }
}
