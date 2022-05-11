package com.lxf.ums.mq.direct;

import com.lxf.ums.mq.cons.DirectConf;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : DirectMQConfig
 * @Description TODO
 * @Date 2022/4/19 21:24
 * @Created lxf
 */
@Configuration
public class DirectMQConfig {



    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DirectConf.REGISTER_EXCHANGE_NAME);
    }

    @Bean
    public Queue directQueue(){
        return new Queue(DirectConf.REGISTER_QUEUE_NAME);
    }

    @Bean
    public Binding directBinding(){
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DirectConf.REGISTER_ROUTING_KEY);
    }


}
