package com.hspedu.secondkill.controller;

import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.GoodsService;
import com.hspedu.secondkill.service.UserService;
import com.hspedu.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 16:51
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Resource
    private RedisTemplate redisTemplate;

    //手动进行渲染需要的模板解析器
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    //原生到db查询
//    @RequestMapping("/toList")
//    // public String toList(HttpSession session, Model model, @CookieValue("userTicket") String userTicket) {
////    public String toList(Model model, @CookieValue("userTicket") String userTicket,
////                         HttpServletRequest request, HttpServletResponse response) {
//    public String toList(Model model, User user) {
//
//        // 判断用户是否登录
////        if (!StringUtils.hasText(userTicket)) {
////            return "login";
////        }
//        //通过ticket获取session中存放user
////        User user = (User) session.getAttribute(userTicket);
////        User user = userService.getUserByCookie(userTicket, request, response);
//
//        if (user == null) {
//            //用户没有成功登录
//            return "login";
//        }
//        model.addAttribute("user", user);
//        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
//        model.addAttribute("goodsList", goodsVo);
//        return "goodsList";
//    }


    /**
     * 使用redis 优化
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,HttpServletRequest request,HttpServletResponse response) {


        if (user == null) {
            //用户没有成功登录
            return "login";
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //先redis获取页面-如果有，直接返回页面
        String html = (String)valueOperations.get("goodsList");
        if(StringUtils.hasText(html)){
           return html;
        }

        model.addAttribute("user", user);
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        model.addAttribute("goodsList", goodsVo);

        //如果从Redis没有获到贡面，手动渲染，并存入redis

        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(StringUtils.hasText(html)){
            //将页面保存到redis,设置每60s更新一次，该页面60s失效，Redis会清除该页面
            valueOperations.set("goodsList", html,60, TimeUnit.SECONDS);
        }

        return html;
    }





    //跳转商品详情页面
    //说明：User user是通过我门自定义的参数解析器处理，返回
    //@PathVariable Long goodsId路径变量，是用户点击详情时，给携带过来的
//    @RequestMapping(value = "/toDetail/{goodsId}")
//    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goodsVo);
//
//
//        //============处理秒杀倒计时和状态 start ==============
//        ////1.变量secKil1 Status秒杀状态  0：秒杀未开始  1：秒杀进行中 2：秒杀已经结束
//        ////2.变量remainSeconds剩余秒数 ：>0：表示还有多久开始秒杀    0：表示秒杀进行中    -1表示秒杀已经结束
//        Date startDate = goodsVo.getStartDate();
//        Date endDate = goodsVo.getEndDate();
//        Date nowDate = new Date();
//        //秒杀状态
//        int secKillStatus = 0;
//        //秒杀倒计时
//        int remainSeconds = 0;
//        if (nowDate.before(startDate)) {
//            //秒杀还没有开始
//            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
//        } else if (nowDate.after(endDate)) {
//            //秒杀结束
//            secKillStatus = 2;
//            remainSeconds = -1;
//        } else {
//            //秒杀进行中
//            secKillStatus = 1;
//            remainSeconds = 0;
//        }
//
//        model.addAttribute("remainSeconds", remainSeconds);
//
//        model.addAttribute("secKillStatus", secKillStatus);
//        return "goodsDetail";
//    }


    // 用redis 优化
    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId,
                           HttpServletRequest request,HttpServletResponse response) {
        if (user == null) {
            return "login";
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsDetail:"+goodsId);
        if(StringUtils.hasText(html)){
            return html;
        }

        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);


        //============处理秒杀倒计时和状态 start ==============
        ////1.变量secKil1 Status秒杀状态  0：秒杀未开始  1：秒杀进行中 2：秒杀已经结束
        ////2.变量remainSeconds剩余秒数 ：>0：表示还有多久开始秒杀    0：表示秒杀进行中    -1表示秒杀已经结束
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            //秒杀还没有开始
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            //秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);


        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(StringUtils.hasText(html)){
            valueOperations.set("goodsDetail:"+goodsId, html,60, TimeUnit.SECONDS);
        }
        return html;
    }
}
