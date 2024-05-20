package com.hspedu.secondkill.controller;

import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.UserService;
import com.hspedu.secondkill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-17 8:57
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @Resource
    private UserService userService;
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user, String address) {
        return RespBean.success(user);
    }

    @RequestMapping("")
    @ResponseBody
    public RespBean updatePasswd(String userTicket, String pasaword, HttpServletRequest request, HttpServletResponse response){
        RespBean respBean = userService.updatePassword(userTicket, pasaword, request, response);
        return respBean;
    }
}
