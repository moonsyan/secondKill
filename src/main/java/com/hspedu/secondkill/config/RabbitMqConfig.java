package com.hspedu.secondkill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-17 17:33
 */
@Configuration
public class RabbitMqConfig {

    public static final String QUEUE = "queue";
    // fanout 模式
    public static final String QUEUE_FANOUT01 = "queue-fanout01";
    public static final String QUEUE_FANOUT02 = "queue-fanout02";
    public static final String EXCHANGE = "fanout-exchange";

    // direct 模式
    public static final String QUEUE_DIRECT01 = "queue-direct01";
    public static final String QUEUE_DIRECT02 = "queue-direct02";
    public static final String DIRECT_EXCHANGE = "direct-exchange";
    public static final String routerKey01 = "routerKey01";
    public static final String routerKey02 = "routerKey02";

    /**
     * 老师解读
     * 1. 配置队列
     * 2. 队列名为 queue
     * 3. true 表示: 持久化
     * durable(默认是 true)： 队列是否持久化。 队列默认是存放到内存中的，rabbitmq 重启则丢失，
     * 若想重启之后还存在则队列要持久化，
     * 保存到 Erlang 自带的 Mnesia 数据库中，当 rabbitmq 重启之后会读取该数据库
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_FANOUT01);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_FANOUT02);
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE);
    }

    /**
     * 将 QUEUE 绑定到交换机EXCHANGE(fanoutExchange)
     */
    @Bean
    public Binding binding03() {

        return BindingBuilder.bind(queue1()).to(exchange());
    }
    @Bean
    public Binding binding04() {

        return BindingBuilder.bind(queue2()).to(exchange());
    }


    /**
     * direct
     */
    @Bean
    public Queue queue01() {
        return new Queue(QUEUE_DIRECT01);
    }
    @Bean
    public Queue queue02() {
        return new Queue(QUEUE_DIRECT02);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }
    @Bean
    public Binding binding01() {

        return BindingBuilder.bind(queue01()).to(directExchange()).with(routerKey01);
    }
    @Bean
    public Binding binding02() {
        return BindingBuilder.bind(queue02()).to(directExchange()).with(routerKey02);
    }


}
