package com.xum.zk.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.io.IOException;
import java.util.List;

public class ZkClientTest {
    public static void main(String[] args) {

        ZkClient client = new ZkClient("192.168.62.111:2181",5000,500);
        client.subscribeChildChanges("/test", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("childChanged:"+s+","+list);
            }
        });
        client.subscribeDataChanges("/test", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("handleDataChange:"+s+","+toString());
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("handleDataDeleted:"+s);
            }
        });
        client.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println("handleStateChanged:"+keeperState);

            }

            @Override
            public void handleNewSession() throws Exception {
                System.out.println("handleNewSession:");
            }
        });
//        String res = client.create("/test","aaa", CreateMode.EPHEMERAL);
//        System.out.println(res+"--create");
//
//        client.createPersistent("/aaaa");
//        client.createPersistent("/aaaa/bbbb","aaa");

        Object o = client.readData("/test");
        System.out.println(o);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
