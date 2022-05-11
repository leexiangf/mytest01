package com.lxf.ums.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName : MyListener
 * @Description  mq消息确认机制
 * @Date 2022/4/19 22:48
 * @Created lxf
 */
@Component
@Slf4j
public class MyListener implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     *     confirm机制是只保证消息到达exchange，并不保证消息可以路由到正确的queue
     *
     *     当前的exchange不存在或者指定的路由key路由不到才会触发return机制
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData ：{}",correlationData.getId());
        if(ack){
            log.info("消息发送成功 ");
        }else {
            log.info("消息发送失败原因,{}",cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("returnedMessage : exchange:{} , routingKey:{}" ,returned.getExchange(),returned.getRoutingKey() );
        log.warn("msg : {}",returned.getMessage().getBody().toString());
        String exchange = returned.getExchange();
        String routingKey = returned.getRoutingKey();
        String msg = returned.getMessage().getBody().toString();
        log.warn("rabbitMQ reSend msg -->exchange :{} ,routingKey :{} ,msg:{}",exchange,routingKey,msg);
        rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }
}
