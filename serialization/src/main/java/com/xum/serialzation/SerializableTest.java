package com.xum.serialzation;

import java.io.*;

public class SerializableTest {


    public static void main(String[] args) {
        User user = new User();
        user.setName("zhangsanfeng");
        user.setAge(12);
        user.setUsername("username");
        user.setPassword("pwd");
        User.count = 100;
        serial(user);
        User.count = 200;
        User o = (User) deSerial();
        System.out.println(o);
        System.out.println(User.count);
        System.out.println(o.getPassword());
        System.out.println(o.getUsername());
    }

    public static void serial(Object obj){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("a.dat")));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deSerial() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File("a.dat")));
            Object obj = ois.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
