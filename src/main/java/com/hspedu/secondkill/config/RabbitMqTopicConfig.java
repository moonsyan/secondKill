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
 * @create 2024-05-17 20:05
 */
@Configuration
public class RabbitMqTopicConfig {
    public static final String QUEUE01 = "queue_topic01";
    public static final String QUEUE02 = "queue_topic02";
    public static final String EXCHANGE = "topicExchange";
    public static final String ROUTINGKEY01 = "#.queue.#";
    public static final String ROUTINGKEY02 = "*.queue.#";
    @Bean
    public Queue queue_topic01() {
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue_topic02() {
        return new Queue(QUEUE02);
    }
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding_topic01() {
        return BindingBuilder.bind(queue_topic01()).to(topicExchange()).with(ROUTINGKEY01);
    }
    @Bean
    public Binding binding_topic02() {
        return BindingBuilder.bind(queue_topic02()).to(topicExchange()).with(ROUTINGKEY02);
    }
}
