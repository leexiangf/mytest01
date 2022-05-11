package com.lxf.ums.mq.direct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxf.ums.mq.cons.DirectConf;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName : DirectConsumer
 * @Description TODO
 * @Date 2022/4/19 21:36
 * @Created lxf
 */
@Configuration
@Slf4j
public class DirectConsumer {

    @RabbitListener(queues = {DirectConf.REGISTER_QUEUE_NAME})
    @RabbitHandler
    public void receiveMessage(String msg, Channel channel, Message originalMessage) throws IOException {
        try {
            //参数（该条消息的索引，是否批量）

            MessageProperties messageProperties = originalMessage.getMessageProperties();
            Map<String, Object> headers = messageProperties.getHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            String head = objectMapper.writeValueAsString(headers);
            String baseToken = (String) headers.get("BASE_TOKEN");
            log.info("get msg :{}",msg);
            log.info("get header :{}",head);
            log.info("get token : {}",baseToken);
            channel.basicAck(originalMessage.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            //参数（该条消息的索引，是否批量，是否将该消息还原到队列）
            channel.basicNack(originalMessage.getMessageProperties().getDeliveryTag(),false,true);
        }

    }

}
