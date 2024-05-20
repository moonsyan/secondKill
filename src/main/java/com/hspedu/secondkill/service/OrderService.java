package com.hspedu.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.secondkill.pojo.Order;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.vo.GoodsVo;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 17:22
 */
public interface OrderService extends IService<Order> {
    //秒杀
    Order seckill(User user, GoodsVo goodsVo);

    //获取秒杀路径
    String createPath(User user, Long goodsId);
    //对秒杀路径进行校验
    boolean checkPath(User user, Long goodsId,String path);

    //验证用户输入的验证码是否正确
    boolean checkCapthcha(User user, Long goodsId, String captcha);
}