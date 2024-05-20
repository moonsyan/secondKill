package com.hspedu.secondkill.controller;

import com.hspedu.secondkill.service.UserService;
import com.hspedu.secondkill.vo.LoginVo;
import com.hspedu.secondkill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 10:48
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    //编写方法，可以进入到登录页面
    @Resource
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    public RespBean doLogin(@Validated LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("loginInfo : {}", loginVo);
        RespBean respBean = userService.doLogin(loginVo, request, response);
        return respBean;
    }
}
