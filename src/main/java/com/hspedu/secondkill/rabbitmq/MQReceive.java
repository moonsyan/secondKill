package com.hspedu.secondkill.rabbitmq;

import com.hspedu.secondkill.config.RabbitMqConfig;
import com.hspedu.secondkill.config.RabbitMqHeaderConfig;
import com.hspedu.secondkill.config.RabbitMqTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-17 17:41
 */

@Service
@Slf4j
public class MQReceive {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE})
    public void receive(Object msg){
        log.info("收到消息:{}", msg);

    }


    @RabbitListener(queues = {RabbitMqConfig.QUEUE_FANOUT01})
    public void receive1(Object msg){
        log.info("QUEUE_FANOUT  01  收到消息:{}", msg);

    }
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_FANOUT02})
    public void receive2(Object msg){
        log.info("QUEUE_FANOUT  02  收到消息:{}", msg);

    }

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_DIRECT01})
    public void receive3(Object msg){
        log.info("QUEUE_DIRECT  01  收到消息:{}", msg);
    }
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_DIRECT02})
    public void receive4(Object msg){
        log.info("QUEUE_DIRECT  02  收到消息:{}", msg);
    }



    @RabbitListener(queues = {RabbitMqTopicConfig.QUEUE01})
    public void receive5(Object msg){
        log.info("queue_topic   01  收到消息:{}", msg);
    }
    @RabbitListener(queues = {RabbitMqTopicConfig.QUEUE02})
    public void receive6(Object msg){
        log.info("queue_topic   02  收到消息:{}", msg);
    }

    @RabbitListener(queues = {RabbitMqHeaderConfig.QUEUE01})
    public void receive7(Message message){
        log.info("QUEUE01 接收消息 message 对象" + message);
        log.info("QUEUE01 接收消息" + new String(message.getBody()));
    }
    @RabbitListener(queues = {RabbitMqHeaderConfig.QUEUE02})
    public void receive8(Message message){
        log.info("QUEUE02 接收消息 message 对象" + message);
        log.info("QUEUE02 接收消息" + new String(message.getBody()));
    }






}
