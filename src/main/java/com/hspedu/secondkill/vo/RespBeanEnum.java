package com.hspedu.secondkill.vo;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 9:55
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "操作成功"), FAILED(500, "服务端异常"),

    //用户登陆注册
    LOGIN_ERROR(500210, "用户名或密码错误"),
    MOBILE_ERROR(500211, "手机号格式不正确"),
    MOBILE_NOT_EXIST(500213, "手机号码不存在"),
    //参数绑定校验异常
    BIND_ERROR(500212, "参数绑定异常"),


    PASSWORD_UPDATE_FAIL(500214, "更新密码失败"),
    //秒杀模块
    ENTRY_STOCK(500500, "库存不足"),
    REPEATE_ERROR(500501, "该商品每人限购一件"),
    //秒杀安全
    SESSION_ERROR(500502, "用户信息有误"),
    REQUEST_ILLEGAL(500503, "请求非法"),
    SEC_KILL_WAIT(500504, "排队中...."),
    ERROR_CAPTHCA(500505, "验证码错误"),
    ACCESS_LIMIT_REACHED(500506, "访问过于频繁"),
    SEC_KILL_RETRY(500507, "秒杀重试"),;



    private final Integer code;
    private final String message;
}
