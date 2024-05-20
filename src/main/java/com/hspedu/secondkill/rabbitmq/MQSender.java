package com.hspedu.secondkill.rabbitmq;

import com.hspedu.secondkill.config.RabbitMqConfig;
import com.hspedu.secondkill.config.RabbitMqHeaderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
public class MQSender {
    //装配rabbitmqTemplate  操作  rabbitmq
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg) {
        log.info("发送的消息：{}", msg);

        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE, msg);


    }

    public void sendFanout(Object msg) {
        log.info("发送的消息：{}", msg);

        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, "", msg);

    }

    public void sendDirect01(Object msg) {
        log.info("发送的消息：{}", msg);
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.routerKey01, msg);
    }

    public void sendDirect02(Object msg) {
        log.info("发送的消息：{}", msg);
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.routerKey02, msg);
    }

    public void send03(Object msg) {
        log.info("发送消息(QUEUE01 接收)：" + msg);
//发送消息到 topicExchange 队列，同时携带 routingKey queue.red.message
        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
    }

    public void send04(Object msg) {
        log.info("发送消息(QUEUE02 接收)：" + msg);
//发送消息到 topicExchange 队列，同时携带 routingKey green.queue.green.message
        rabbitTemplate.convertAndSend("topicExchange", "green.queue.green.message", msg);
    }

    public void sendHeader01(String msg) {
        log.info("发送消息(QUEUE01 接收)：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color", "yellow");
        properties.setHeader("speed", "normal");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend(RabbitMqHeaderConfig.EXCHANGE, "", message);
    }


    public void sendHeader02(String msg) {
        log.info("发送消息(QUEUE01 和 QUEUE02 接收)：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color", "yellow");
        properties.setHeader("speed", "fast");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend(RabbitMqHeaderConfig.EXCHANGE, "", message);
    }




}
