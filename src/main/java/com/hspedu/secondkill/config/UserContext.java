package com.hspedu.secondkill.config;

import com.hspedu.secondkill.pojo.User;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 16:01
 */
public class UserContext {
    //每个线程都有自己的ThreadLocal,把共享数据存放到这里，保证线程安全
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
       return userHolder.get();
    }


}
