package com.hspedu.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.vo.LoginVo;
import com.hspedu.secondkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 10:35
 */
public interface UserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


    //根据Cookie-ticket获取用户
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);


    //更新密码
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
