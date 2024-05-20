package com.hspedu.secondkill.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.hspedu.secondkill.config.RabbitMqSecondKillConfig;
import com.hspedu.secondkill.pojo.SecondKillMessage;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.GoodsService;
import com.hspedu.secondkill.service.OrderService;
import com.hspedu.secondkill.service.SeckillGoodsService;
import com.hspedu.secondkill.service.SeckillOrderService;
import com.hspedu.secondkill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 8:53
 */
@Slf4j
@Service
public class MQSeckillMessageReceive {

    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;

    //下单操作
    @RabbitListener(queues = RabbitMqSecondKillConfig.quene)
    public void queue(String message) {
        log.info("接收到的消息：" + message);
        SecondKillMessage seckillMessage =
                JSONUtil.toBean(message, SecondKillMessage.class);
        Long goodId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
//获取抢购的商品信息
        GoodsVo goodsVo =
                goodsService.findGoodsVoByGoodsId(goodId);
//下单操作
        orderService.seckill(user, goodsVo);
    }
}
