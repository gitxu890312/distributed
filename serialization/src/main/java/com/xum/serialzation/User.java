package com.xum.serialzation;



import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
public class User extends Person implements Serializable {
    public static final long serialVersionUID = 1l;
    /**
     * 用户个数
     * 测试静态成员序列化
     */
    public static int count = 100;

    private  String username;
    //测试transient 关键字
    private transient String password;


    public User() {
        System.out.println(" User construct");
    }

}

