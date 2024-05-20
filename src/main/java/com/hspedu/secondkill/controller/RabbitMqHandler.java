package com.hspedu.secondkill.controller;

import com.hspedu.secondkill.rabbitmq.MQSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-17 17:49
 */
@Controller
public class RabbitMqHandler {
@Resource
    private MQSender mqSender;

    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("hello rabbitmq");
    }
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq1(){
        mqSender.sendFanout("hello rabbitmq fanout");
    }
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq2(){
        mqSender.sendDirect01("hello rabbitmq direct 01 ");
    }
    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq3(){
        mqSender.sendDirect02("hello rabbitmq direct 02 ");
    }


    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mq4(){
        mqSender.send03("hello  topic  ");
    }


    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mq5(){
        mqSender.send04("hello  topic  ");
    }


    @RequestMapping("/mq/header01")
    @ResponseBody
    public void mq6(){
        mqSender.sendHeader01("hello header ");
    }


    @RequestMapping("/mq/header02")
    @ResponseBody
    public void mq7(){
        mqSender.sendHeader02("hello header ");
    }



}
