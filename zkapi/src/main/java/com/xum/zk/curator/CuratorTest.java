package com.xum.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorTest {
    public static void main(String[] args) throws Exception {
        CuratorFramework build = CuratorFrameworkFactory.builder().connectString("192.168.62.111:2181").connectionTimeoutMs(5000).sessionTimeoutMs(5000)
        //重试策略
                .retryPolicy(new ExponentialBackoffRetry(2000,3)).build();
        build.start();
//        CreateBuilder createBuilder = build.create();
//        String s = createBuilder.forPath("/test", "aaa".getBytes());
//        System.out.println(s);
        byte[] bytes = build.getData().forPath("/test");
        System.out.println(new String(bytes));
    }
}
