package com.xum.zk.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ACLTest {
    public static final String HOST = "192.168.62.111:2181";

    public static void main(String[] args) {

        CountDownLatch count = new CountDownLatch(1);
        Watcher watcher  = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event+"---");
                if(event.getType()== Event.EventType.None){
                    System.out.println("链接成功");
                    count.countDown();
                }

            }
        };
        try {
            ZooKeeper zk = new ZooKeeper(HOST,5000,watcher);

            count.await();
            /**
             * 创建指定权限的节点
             */
            System.out.println("创建节点");
            zk.create("/test" ,"aaa".getBytes(),createAcl("ip","192.168.62.112", ZooDefs.Perms.READ|ZooDefs.Perms.WRITE), CreateMode.PERSISTENT);
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param schema 权限方案
     * @param id id
     * @param permission 权限类型
     * @return
     */
    public static List<ACL> createAcl(String schema, String id, int permission){

        Id idobj = new Id(schema,id);
        ACL acl = new ACL(permission,idobj);
        return new ArrayList<ACL>(
                Collections.singletonList(acl));
    }
}
