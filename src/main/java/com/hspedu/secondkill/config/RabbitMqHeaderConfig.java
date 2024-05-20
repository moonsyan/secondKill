package com.hspedu.secondkill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-18 9:23
 */
@Configuration
public class RabbitMqHeaderConfig {
    public static final String QUEUE01 = "queue_header01";
    public static final String QUEUE02 = "queue_header02";
    public static final String EXCHANGE = "headersExchange";

    @Bean
    public Queue queue_header01() {
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue_header02() {
        return new Queue(QUEUE02);
    }
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(EXCHANGE);
    }


    @Bean
    public Binding binding_header01() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", "yellow");
        map.put("speed", "low");
        return BindingBuilder.bind(queue_header01()).to(headersExchange()).whereAny(map).match();

    }

    @Bean
    public Binding binding_header02() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", "yellow");
        map.put("speed", "fast");
        return BindingBuilder.bind(queue_header02()).to(headersExchange()).whereAll(map).match();
    }



}
