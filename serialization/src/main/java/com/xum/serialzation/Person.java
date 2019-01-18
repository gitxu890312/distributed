package com.xum.serialzation;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试父类是否序列化
 */
@Data
public class Person implements Serializable
{
    private  String name;

    private int age;
}
