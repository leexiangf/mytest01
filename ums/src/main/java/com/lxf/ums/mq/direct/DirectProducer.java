package com.lxf.ums.mq.direct;

import com.lxf.ums.mq.cons.DirectConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @ClassName : DirectProducer
 * @Description TODO
 * @Date 2022/4/19 21:42
 * @Created lxf
 */
@Component
@Slf4j
public class DirectProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String msg){
        rabbitTemplate.convertAndSend(DirectConf.REGISTER_EXCHANGE_NAME,
                DirectConf.REGISTER_ROUTING_KEY,msg,new CorrelationData(UUID.randomUUID().toString()));
    }

    public void sendMessageWithProperties(String msg){
        rabbitTemplate.convertAndSend(DirectConf.REGISTER_EXCHANGE_NAME,DirectConf.REGISTER_ROUTING_KEY,msg,
                originalMessage ->{
                    MessageProperties messageProperties = originalMessage.getMessageProperties();
                    messageProperties.setHeader("BASE_TOKEN","0909090909");
                    return originalMessage;
                },new CorrelationData(UUID.randomUUID().toString()));
    }
}
