package com.hspedu.secondkill.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspedu.secondkill.config.AccessLimit;
import com.hspedu.secondkill.config.UserContext;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.UserService;
import com.hspedu.secondkill.util.CookieUtil;
import com.hspedu.secondkill.vo.RespBean;
import com.hspedu.secondkill.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 15:54
 */
@Component
public class MyIntercepter implements HandlerInterceptor {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    //这个方法完成.得到 user对象，并放入到ThreadLoacl2.去处0 Accesslimit
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            //这里我门就先获取到登录的User对象
            User user = getUserTicket(request, response);

            //将user放入到ThreadLocal中
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;

            //得到方法上的注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                //如果目标方法没有AcpessLimit说明该接口并没有处理限流防刷
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            if(needLogin){  //说明用户必须登录才能访问目标方法/接口
                if(user == null){
                    //返回一个用户信息错误的提示
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }

                String uri = request.getRequestURI();
                ValueOperations valueOperations = redisTemplate.opsForValue();
                Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
                if (count == null) { //说明还没有key,就初始化
                    valueOperations.set(uri + ":" + user.getId(), 1, seconds, TimeUnit.SECONDS);
                } else if (count < 5) {
                    valueOperations.increment(uri + ":" + user.getId());
                } else {
                    //  返回频繁访问的提示
                    render(response, RespBeanEnum.ACCESS_LIMIT_REACHED);
                    return false;
                }


            }
        }

        return true;
    }

    //构建返回对象-以流的形式返回
    public void render(HttpServletResponse response,RespBeanEnum respBeanEnum) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        RespBean error = RespBean.error(respBeanEnum);
        writer.write(new ObjectMapper().writeValueAsString(error));
        writer.flush();
        writer.close();
    }

    //单独编写方法，得到登录User对象-userTicket
    public User getUserTicket(HttpServletRequest request,HttpServletResponse response){
        //得到cookie
        String userTicket = CookieUtil.getCookieValue(request, "userTicket");
        if(!StringUtils.hasText(userTicket)) {
            return null;
        }
        User userByCookie = userService.getUserByCookie(userTicket, request, response);

        return userByCookie;
    }

}
