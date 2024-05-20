package com.hspedu.secondkill.rabbitmq;

import com.hspedu.secondkill.config.RabbitMqSecondKillConfig;
import com.hspedu.secondkill.pojo.SecondKillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 8:49
 */
@Slf4j
@Service
public class MQSeckillMessageSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(String message){
        log.info("发送消息: {}", message);
        rabbitTemplate.convertAndSend(RabbitMqSecondKillConfig.EXCHANGE, "seckill.message", message);
    }
}
