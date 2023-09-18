package com.lxf.netty_server.initializer;

import com.lxf.netty_server.handle.NettyServer2Handler;
import com.lxf.netty_server.handle.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Classname NettyServerInitializer
 * @Description 连接监控
 * @Date 2023/9/14 16:05
 * @Author lxf
 */
@Component
@Slf4j
public class NettyServer2Initializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServer2Handler nettyServerHandler;

    //连接注册，创建成功，会被调用
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("==================netty报告==================");
        log.info("信息：客户端链接");
        log.info("==================netty报告完毕==================");
        ChannelPipeline pipeline = ch.pipeline();
        //定义读写空闲时间——（单位秒）
       // pipeline.addLast(new IdleStateHandler(180, 60,180));
        //向pipeline加入解码器
        pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
        //向pipeline加入编码器
        pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
        //注册拦截器
        pipeline.addLast(nettyServerHandler);
    }
}
