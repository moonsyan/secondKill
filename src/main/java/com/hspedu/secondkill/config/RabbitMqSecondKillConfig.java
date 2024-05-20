package com.hspedu.secondkill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 8:45
 */
//配置类，创建消息队列和交换机
@Configuration
public class RabbitMqSecondKillConfig {
    public static final String quene = "seckillQuene";


    public static final String EXCHANGE = "seckillExchange";
    @Bean
    public Queue queue_seckill() {
        return new Queue(quene);
    }
    @Bean
    public TopicExchange topicExchange_seckill() {
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding_seckill() {
        return BindingBuilder.bind(queue_seckill())
                .to(topicExchange_seckill()).with("seckill.#");
    }
}
