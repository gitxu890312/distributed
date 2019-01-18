package com.xum.serialzation.serial.framework;

import com.alibaba.fastjson.JSON;
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
    public static int count = 100000;
    public static void main(String[] args) {
        init();
        serialJava();
        serialFastJson();
        serialJackSon();
        serialgson();
    }
    public static void init(){
        for(int i=0;i<count;i++){
            Person person = new Person();
            person.setAge(i);
            person.setName(i+"zhangsan");
            lists.add(person);
        }
    }
    public static void serialJava() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream bos = null;
        try {
            bos = new ObjectOutputStream(baos);
            Long start = System.currentTimeMillis();
            for (Object item : lists) {
                bos.writeObject(item);
            }
            System.out.println("javaSerial:"+(System.currentTimeMillis()-start));
            System.out.println("javaSerialsize:"+baos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void serialFastJson(){
        String text = "";
        Long start = System.currentTimeMillis();
        long count = 0;
        for(Person item:lists){
            byte[] bytes = JSON.toJSONBytes(item);
            count+=bytes.length;
        }
        System.out.println("fastJson:"+(System.currentTimeMillis()-start));
        System.out.println("fastJsonsize:"+(count));
    }
    public static void serialJackSon(){
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Long start = System.currentTimeMillis();
        for(Person item:lists){
            try {
                mapper.writeValue(out,item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("JackSon:"+(System.currentTimeMillis()-start));
        System.out.println("JackSonsize:"+out.size());
    }
    public static void serialgson(){
        Gson gson = new Gson();
        String s = null;
        Long start = System.currentTimeMillis();
        for(Person item:lists){
             s = gson.toJson(item);
        }
        System.out.println("gson:"+(System.currentTimeMillis()-start));
        System.out.println("gsonsize:"+(count*s.getBytes().length));
    }
}
