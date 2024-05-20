package com.hspedu.secondkill.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hspedu.secondkill.config.AccessLimit;
import com.hspedu.secondkill.pojo.Order;
import com.hspedu.secondkill.pojo.SeckillOrder;
import com.hspedu.secondkill.pojo.SecondKillMessage;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.rabbitmq.MQSeckillMessageSender;
import com.hspedu.secondkill.service.GoodsService;
import com.hspedu.secondkill.service.OrderService;
import com.hspedu.secondkill.service.SeckillOrderService;
import com.hspedu.secondkill.vo.GoodsVo;
import com.hspedu.secondkill.vo.RespBean;
import com.hspedu.secondkill.vo.RespBeanEnum;
import com.ramostear.captcha.HappyCaptcha;
import com.ramostear.captcha.common.Fonts;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 18:25
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private OrderService orderService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisScript redisScript;
    @Resource
    private MQSeckillMessageSender mqSeckillMessageSender;

    ///定义map-记录種杀商品是否还有库存
    private Map<Long, Boolean> entrystockMap = new HashMap<>();

//    @RequestMapping(value = "/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//        System.out.println("-----秒杀 V1.0--------");
////===================秒杀 v1.0 start =========================
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        //解决重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
//                        .eq("goods_id", goodsId));
//        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//
//        //抢购 ,问题所在！！！！ 生成订单这一步
//        Order order = orderService.seckill(user, goodsVo);
//
//
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//        return "orderDetail";
////===================秒杀 v1.0 end... =========================
//    }


//    @RequestMapping(value = "/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//        System.out.println("-----秒杀 V2.0--------");
////===================秒杀 v2.0 start =========================
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        //解决重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
////                        .eq("goods_id", goodsId));
//        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
//
//        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        //判断是否已经秒杀到了
//        //抢购 ,问题所在！！！！ 生成订单这一步
//        Order order = orderService.seckill(user, goodsVo);
//
//
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//        return "orderDetail";
////===================秒杀 v2.0 end... =========================
//    }


//    @RequestMapping(value = "/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//        System.out.println("-----秒杀 V3.0--------");
////===================秒杀 v3.0 start =========================
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        //解决重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
////                        .eq("goods_id", goodsId));
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
//
//
//        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        //map进行判断[内存标记]，如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
//        if(entrystockMap.get(goodsId)){
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        /**
//         * ！！！变化，在执行seckill 方法之前，进行库存预减，要是库存不够了就直接返回，不要再去操作mysql,防止线程堆积，优化高并发
//         */
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {
//
//            //说明当前秒杀的商品，己经没有库存
//            entrystockMap.put(goodsId, true);
//
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        //判断是否已经秒杀到了
//        //抢购 ,问题所在！！！！ 生成订单这一步
//        Order order = orderService.seckill(user, goodsVo);
//
//
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//        return "orderDetail";
////===================秒杀 v3.0 end... =========================
//    }

    @RequestMapping(value = "/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {
//        System.out.println("-----秒杀 V4.0--------");
//===================秒杀 v3.0 start =========================
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsId", goodsId);

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
            return "secKillFail";
        }


        //解决重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
//                        .eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());


        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        //map进行判断[内存标记]，如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
        if (entrystockMap.get(goodsId)) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        /**
         * ！！！变化，在执行seckill 方法之前，进行库存预减，要是库存不够了就直接返回，不要再去操作mysql,防止线程堆积，优化高并发
         */
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {
//
//            //说明当前秒杀的商品，己经没有库存
//            entrystockMap.put(goodsId, true);
//
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }


        //============使用 Redis 分布式锁=========
        //老师说明
        //1. 对于当前项目,使用 redisTemplate.opsForValue().decrement()就可以控制
        // 2. 考虑到如果有比较多的操作，需要保证隔离性, 和对 Redis 操作也不是简单的-1，而是有多个操作,
        // 使用 redis 分布式锁来控制

        //1 获取锁，setnx
        //得到一个 uuid 值，作为锁的值


        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        //2 获取锁成功、就执行相应业务

        if (lock) {
//            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//            // 使用 redis 执行 lua 执行
//            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//            redisScript.setScriptText(script);
//            redisScript.setResultType(Long.class);
            Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);


            if (decrement < 0) {
                //说明当前秒杀的商品，己经没有库存
                entrystockMap.put(goodsId, true);
                //恢复库存为0
                redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);

                //释放锁 --lua脚本
                redisTemplate.execute(redisScript, Arrays.asList("lock"), uuid);

                model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
                return "secKillFail";
            }
            //释放分布式锁
            redisTemplate.execute(redisScript, Arrays.asList("lock"), uuid);

        } else {
            model.addAttribute("errmsg", RespBeanEnum.SEC_KILL_RETRY.getMessage());
            return "secKillFail";//错误页面
        }


        //抢购，向消息队列发送秒杀请求，实现了秒杀异步请求
        //这里我们发送秒杀消息后，立即快速返回结果[临时结果]
        //客户端可以通过轮询，获得最终结果
        SecondKillMessage secondKillMessage = new SecondKillMessage(user, goodsId);
        mqSeckillMessageSender.sendSeckillMessage(JSONUtil.toJsonStr(secondKillMessage));
        model.addAttribute("errmsg", "排队中....");
        return "secKillFail";
//===================秒杀 v4.0 end... =========================
    }
//
//
//
//
//    /**
//     * 该方法是在类的所有属性，都初始化之后，自动执行的（在spring 中讲过）
//     *
//     * @throws Exception
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        //初始化库存信息
//        List<GoodsVo> list = goodsService.findGoodsVo();
//        if (CollectionUtils.isEmpty(list)) {
//            return;
//        }
//        //遍历 list ，然后将秒杀商品的库存量，放入redis
//        //key： seckillGoods:商品id   value：库存量
//        for (GoodsVo goodsVo : list) {
//            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
//            //当有库存为 false，
//            // 当无库存为 true。
//            // 防止库存没有了，
//            entrystockMap.put(goodsVo.getId(), false);
//        }
//        System.out.println("-----初始化库存信息完成--------");
//    }


//    @RequestMapping(value = "/{path}/doSeckill")
//    //说明：我们先完成一个V6.0版本，加入消秒杀安全，直接返回RespBear
//    @ResponseBody
//    public RespBean doSeckill(Model model, User user, Long goodsId, @PathVariable("path") String path) {
////        System.out.println("-----秒杀 V5.0--------");
//
//        if (user == null) {
//            // return "login";
//            return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        }
//
//        //这里增加一个判断逻辑，判断请求路径是否合法
//        boolean b = orderService.checkPath(user, goodsId, path);
//        if (!b) {
//            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
//        }
//
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        //判断库存
//        if (goodsVo.getStockCount() < 1) {
//
//            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
//
//        }
//
//
//        //解决重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
////                        .eq("goods_id", goodsId));
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
//
//
//        if (seckillOrder != null) {
//
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }
//
//        //map进行判断[内存标记]，如果商品在map已经标记为没有库存，则直接返回，无需进行Redis预减
//        if (entrystockMap.get(goodsId)) {
//
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }
//
//        /**
//         * ！！！变化，在执行seckill 方法之前，进行库存预减，要是库存不够了就直接返回，不要再去操作mysql,防止线程堆积，优化高并发
//         */
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
//        if (decrement < 0) {
//
//            //说明当前秒杀的商品，己经没有库存
//            entrystockMap.put(goodsId, true);
//
//            //恢复库存为0
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
//
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }
//
//        //抢购，向消息队列发送秒杀请求，实现了秒杀异步请求
//        //这里我们发送秒杀消息后，立即快速返回结果[临时结果]
//        //客户端可以通过轮询，获得最终结果
//        SecondKillMessage secondKillMessage = new SecondKillMessage(user, goodsId);
//        mqSeckillMessageSender.sendSeckillMessage(JSONUtil.toJsonStr(secondKillMessage));
//
//
//        return RespBean.error(RespBeanEnum.SEC_KILL_WAIT);
//
//    }


    /**
     * 该方法是在类的所有属性，都初始化之后，自动执行的（在spring 中讲过）
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化库存信息
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //遍历 list ，然后将秒杀商品的库存量，放入redis
        //key： seckillGoods:商品id   value：库存量
        for (GoodsVo goodsVo : list) {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            //当有库存为 false，
            // 当无库存为 true。
            // 防止库存没有了，
            entrystockMap.put(goodsVo.getId(), false);
        }
        System.out.println("-----初始化库存信息完成--------");
    }

    // 查询秒杀结果信息
    @RequestMapping("/getResult")
    public String getResult(Model model, Long userId, Long goodsId) {

        Object o = redisTemplate.opsForValue().get("order:" + userId + ":" + goodsId);
        if (o != null) {
            //此时秒杀成功
            model.addAttribute("orderStatus", 1);
            model.addAttribute("errmsg", "秒杀成功");
        } else if (StringUtils.hasText((String) redisTemplate.opsForValue().get("secondFail:" + userId + ":" + goodsId))) {
            //失败
            model.addAttribute("orderStatus", -1);
            model.addAttribute("errmsg", "秒杀失败");
        } else {
            //排队中
            model.addAttribute("orderStatus", 0);
            model.addAttribute("errmsg", "排队中....");
        }
        return "secKillFail";
    }

    //获取秒杀路径
    @RequestMapping("/path")
    @ResponseBody
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    /**
     *   @AccessLimit(second = 5, maxCount = 5, needLogin = true)
     * 1.使用注解的方式完成附用户的限流防刷-通用性和灵活性提高
     */
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null || goodsId <= 0) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }


        //增加业务逻辑：加入Redis计数器，完成对用户的限流防刷
        //比如：5秒内访问次数超过了5次，我们就认为是刷接口
        //这里老师先把代码写在方法中，后面我们使用注解提高使用的通用性
        //uri就是  localhost:8080/seckill/path  的  /seckill/path
//        String uri = request.getRequestURI();
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
//        if (count == null) { //说明还没有key,就初始化
//            valueOperations.set(uri + ":" + user.getId(), 1, 5, TimeUnit.SECONDS);
//        } else if (count < 5) {
//            valueOperations.increment(uri + ":" + user.getId());
//        } else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
//        }


        // 增加逻辑，校验用户输入的验证码
        boolean b = orderService.checkCapthcha(user, goodsId, captcha);
        if (!b) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTHCA);
        }


        String path = orderService.createPath(user, goodsId);
        return RespBean.success(path);
    }

    //生成校验码
    @GetMapping("/captcha")
    public void happyCaptcha(User user, Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("请求来了... goodsId=" + goodsId);
        HappyCaptcha.require(request, response)
                .style(CaptchaStyle.ANIM) //设置展现样式为动画
                .type(CaptchaType.NUMBER) //设置验证码内容为数字
                .length(6) //设置字符长度为6
                .width(220) //设置动画宽度为220
                .height(80) //设置动画高度为80
                .font(Fonts.getInstance().zhFont()) //设置汉字的字体
                .build().finish(); //生成并输出验证码//把验证码的值存放到 Redis 中, userId+goodsId, 有效时间为 100s


        redisTemplate.opsForValue().set
                ("captcha:" + user.getId() + ":" + goodsId,
                        (String) request.getSession().getAttribute("happy-captcha"),
                        100, TimeUnit.SECONDS);
    }


}