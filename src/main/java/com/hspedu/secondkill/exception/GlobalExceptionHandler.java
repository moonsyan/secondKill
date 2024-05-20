package com.hspedu.secondkill.exception;

import com.hspedu.secondkill.vo.RespBean;
import com.hspedu.secondkill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 15:19
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理所有的异常
    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e) {
        //如果是全局异常，正常处理
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        } else if (e instanceof BindException) {
            //如果是绑定异常 ：由于我们自定义的注解只会在控制台打印错误信息，想让改信息传给前端。
            //需要获取改异常 BindException，进行打印
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage(" 参 数 校 验 异 常 ~ ： " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        return RespBean.error(RespBeanEnum.FAILED);
    }


}

