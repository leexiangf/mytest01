package com.lxf.netty_server.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @Classname NettyHandler
 * @Description
 * @Date 2023/9/14 16:07
 * @Author lxf
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServer2Handler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    //表示 channel 处于就绪状态, 提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    //表示 channel 处于不活动状态, 提示离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("channel : {}",channel.isActive());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
       // String msg = byteBuf.toString(StandardCharsets.UTF_8);
        log.info("收到消息：{}",msg.text());
        //获取到当前 channel
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught:{}",cause.getMessage());
        //关闭通道
        ctx.close();
    }


}
