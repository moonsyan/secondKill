package com.hspedu.secondkill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.secondkill.exception.GlobalException;
import com.hspedu.secondkill.mapper.UserMapper;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.UserService;
import com.hspedu.secondkill.util.CookieUtil;
import com.hspedu.secondkill.util.MD5Util;
import com.hspedu.secondkill.util.UuidUtil;
import com.hspedu.secondkill.util.ValidatorUtil;
import com.hspedu.secondkill.vo.LoginVo;
import com.hspedu.secondkill.vo.RespBean;
import com.hspedu.secondkill.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 10:36
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        
        // 判断手机号和密码是否为空
//        if( !StringUtils.hasText(mobile) || !StringUtils.hasText(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        //校验手机号
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        User user = userMapper.selectById(mobile);
        if(user == null){
          //  return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //用户存在就比较密码
        if(!MD5Util.MidPassToDBPass(password, user.getSlat()).equals(user.getPassword())){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }


        //给每个用户生成ticket-唯一
        String userTicket = UuidUtil.getUuid();
        //将登录成功的用户保存到session
       // request.getSession().setAttribute(userTicket, user);

        //将ticket保存到redis中
        redisTemplate.opsForValue().set("user:" + userTicket ,user);


        //讲ticket保存到cookie
        CookieUtil.setCookie(request,response,"userTicket",userTicket);


        return RespBean.success(userTicket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(!StringUtils.hasText(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        //如果用户不为nUL1,就重新设置cookie,刷新
        if(user != null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }

        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {




        User user = getUserByCookie(userTicket, request, response);
        if(user == null){
            //抛出异常
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSlat()));
        int i = userMapper.updateById(user);
        if(i == 1){
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }else {
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }
    }
}
