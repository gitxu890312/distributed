package com.xum.zk.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeChildrenChanged;

/**
 *
 */
public class ZKAPITest implements Watcher{
    //zk地址
    public static final String HOST = "192.168.62.111:2181";
    //监控节点
    private static final String ROOTPATH = "/zkapi";
    private static final String SUCCESS = "SUCCESS";
    private static final String RESULT = "result";
    private static ZooKeeper zk;
    private static CountDownLatch latch ;

    private static LinkedBlockingMap<String,String> result = new LinkedBlockingMap<>();
    public static void main(String[] args) {
        try {
            init();
            CountDownLatch latch1 =new CountDownLatch(1) ;
            latch1.await();
            System.out.println("testCreate");
            String s = zk.create(ROOTPATH, "TEST".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("createReturn:"+s);
            String res = result.get(RESULT,10, TimeUnit.SECONDS);
            printLog(res,"创建节点");

            System.out.println("--------------------");
            String s1 = zk.create(ROOTPATH+"/child1", "TEST".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("createChildReturn:"+s1);
            String res1 = result.get(RESULT,10, TimeUnit.SECONDS);
            printLog(res1,"创建子节点");


            System.out.println("--------------------");
            //version -1不对节点的数据版本做校验，
            Stat stat = zk.setData(ROOTPATH, "abc".getBytes(), -1);
            System.out.println("setDataReturn:"+stat);
            String res2 = result.get(RESULT,10, TimeUnit.SECONDS);
            printLog(res2,"修改节点数据");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public ZKAPITest(){
        System.out.println("construct");
    }
    public static void init(){
        latch = new CountDownLatch(1);
        ZKAPITest watch = new ZKAPITest();
        try {
            zk = new ZooKeeper(HOST, 5000, watch);
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void printLog(String res,String msg){
        if(SUCCESS.equals(res)){
            System.out.println(msg+"成功");
        }else{
            System.out.println(msg+"失败");
        }
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("==="+watchedEvent);
        if (watchedEvent.getType() != Event.EventType.None) {
            try {
                Event.EventType eventType = watchedEvent.getType();
                switch (eventType){
                    case NodeChildrenChanged:
                        byte[] data3 = zk.getData(watchedEvent.getPath(), true, new Stat());
                        System.out.println("NodeChildrenChangedEvent:"+watchedEvent.getPath()+",data="+new String(data3));
                        result.put(RESULT,SUCCESS);
                        break;
                    case NodeCreated:
                        //getData不回注册子节点的创建事件
                        Stat stat = new Stat();
                        byte[] data = zk.getData(watchedEvent.getPath(), true, stat);
                        System.out.println("stat"+stat);
                        System.out.println("永久节点"+(stat.getEphemeralOwner()==0));
                        //绑定子节点的创建事件
                        zk.getChildren(watchedEvent.getPath(), true);
                        System.out.println("createEvent:"+watchedEvent.getPath()+",data="+new String(data));
                        result.put(RESULT,SUCCESS);
                        break;
                    case NodeDataChanged:
                        byte[] data2 = zk.getData(watchedEvent.getPath(), true, new Stat());
                        System.out.println("NodeDataChangedEvent:"+watchedEvent.getPath()+",data="+new String(data2));
                        result.put(RESULT,SUCCESS);
                    case NodeDeleted:
                        System.out.println("deleteEvent:"+watchedEvent.getPath());
                        result.put(RESULT,SUCCESS);
                        break;
                    case None:
                        Event.KeeperState state = watchedEvent.getState();
                        System.out.println("None:"+watchedEvent.getPath()+",state="+state);
                        break;

                }
                byte[] data = zk.getData(watchedEvent.getPath(), true, new Stat());
                System.out.println(watchedEvent.getPath() + "=" + new String(data));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("登录成功事件");
            System.out.println("type=" + watchedEvent.getType());
            System.out.println("state=" + watchedEvent.getState());
            System.out.println("------");
            try {
                //注册指定节点的监听
                zk.exists(ROOTPATH,true);
                latch.countDown();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
