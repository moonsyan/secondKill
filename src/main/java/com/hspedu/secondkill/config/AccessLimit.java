package com.hspedu.secondkill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 15:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    int seconds(); // 时间范围
    int maxCount(); // 最大访问次数
    boolean needLogin() default true; //是否登录


}
