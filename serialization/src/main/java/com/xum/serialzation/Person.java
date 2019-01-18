package com.xum.serialzation;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Data;

import java.io.Serializable;

/**
 * 测试父类是否序列化
 */
@Data
public class Person implements Serializable
{
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String name;
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int age;
}
