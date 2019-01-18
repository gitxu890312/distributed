package com.xum.serialzation.serial.framework;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.gson.Gson;
import com.xum.serialzation.Person;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
public class FrameworkTest {
    public static List<Person> lists = new ArrayList<>();
    public static int count = 1000000;
    public static void main(String[] args) throws IOException {
        init();
//        serialJava();
        serialFastJson();
        serialJackSon();
        serialgson();
        excuteWithProtoBuf();
    }
    public static void init(){
        for(int i=0;i<count;i++){
            Person person = new Person();
            person.setAge(i);
            person.setName(i+"zhangsan");
            lists.add(person);
        }
    }
//    public static void serialJava() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream bos = null;
//        try {
//            bos = new ObjectOutputStream(baos);
//            Long start = System.currentTimeMillis();
//            byte[] bytes = null;
//            for (Object item : lists) {
//                bos.writeObject(item);
//            }
//            System.out.println("javaSerial:"+(System.currentTimeMillis()-start));
//            System.out.println("javaSerialsize:"+baos.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    private static void excuteWithProtoBuf() throws IOException {
        Codec<Person> personCodec= ProtobufProxy.create(Person.class,false);

        Long start=System.currentTimeMillis();
        byte[] bytes=null;
        for(Person person:lists){
            bytes=personCodec.encode(person);
        }
        System.out.println("protobuf序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+bytes.length);

        Person person1=personCodec.decode(bytes);
        System.out.println(person1);
    }
    public static void serialFastJson(){
        String text = "";
//        byte[] bytes = null;
        Long start = System.currentTimeMillis();
        for(Person item:lists){
            text =  JSON.toJSONString(item);
//          bytes = JSON.toJSONBytes(item);
        }
        System.out.println("fastJson:"+(System.currentTimeMillis()-start));
        System.out.println("fastJsonsize:"+(text.getBytes().length));
    }
    public static void serialJackSon(){
        ObjectMapper mapper = new ObjectMapper();
       byte[] bytes= null;
        Long start = System.currentTimeMillis();
        for(Person item:lists){
            try {
                bytes =   mapper.writeValueAsBytes(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("JackSon:"+(System.currentTimeMillis()-start));
        System.out.println("JackSonsize:"+bytes.length);
    }
    public static void serialgson(){
        Gson gson = new Gson();
        String s = null;
        Long start = System.currentTimeMillis();
        for(Person item:lists){
             s = gson.toJson(item);
        }
        System.out.println("gson:"+(System.currentTimeMillis()-start));
        System.out.println("gsonsize:"+(s.getBytes().length));
    }
}
