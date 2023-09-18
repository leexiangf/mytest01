package com.lxf.netty_server.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Classname NettyHandler
 * @Description
 * @Date 2023/9/14 16:07
 * @Author lxf
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {



    //GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示 channel 处于就绪状态, 提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        //该方法会将 channelGroup 中所有的 channel 遍历，并发送消息
        channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress()+ " 上线了 " + sdf.format(new java.util.Date())+ "\n");
        //将当前 channel 加入到 channelGroup
        channelGroup.add(channel);
        log.info("channelActive : "+ctx.channel().remoteAddress() + " 上线了"+ "\n");
    }

    //表示 channel 处于不活动状态, 提示离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
       // log.info("channel : {}",channel.isActive());
        //将客户离开信息推送给当前在线的客户
        channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 下线了"+ "\n");
        log.info("channelInactive : "+ctx.channel().remoteAddress() + " 下线了"+ "\n");
        log.info("channelInactive : channelGroup size=" + channelGroup.size());
        removeHeart(channel.id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
       // String msg = byteBuf.toString(StandardCharsets.UTF_8);
        log.info("收到消息：{}",msg);
        //获取到当前 channel
        Channel channel = ctx.channel();
        resetHeart(channel.id().asShortText());
        //这时我们遍历 channelGroup, 根据不同的情况， 回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) { //不是当前的 channel,转发消息
                ch.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 发送了消息：" + msg + "\n");
            } else {//回显自己发送的消息给自己
                ch.writeAndFlush("[ 自己 ]发送了消息：" + msg + "\n");
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught:{}",cause.getMessage());
        //关闭通道
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.flush();
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        log.warn("userEventTriggered : channelId : {}",channelId);
        IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
        String state = null;
        switch (idleStateEvent.state()){
            case READER_IDLE: state = "reader";break;
            case WRITER_IDLE: state = "writer";break;
            case ALL_IDLE: state = "all";break;
            default: break;
        }
        log.warn(ctx.channel().remoteAddress()+" : 超时处理 --> "+state);

        if(getHeartCount(channelId) > 2){
            log.warn("max heart to close channel :{}",channelId);
            ctx.channel().writeAndFlush("close channel event");
            ctx.channel().close();
            removeHeart(channelId);
        }
    }

    Map<String ,AtomicInteger> heartMap = new ConcurrentHashMap<>();

    private int getHeartCount(String channelId){
        AtomicInteger heartInt = heartMap.get(channelId);
        if(heartInt == null) {
            heartInt = new AtomicInteger(0);
            heartMap.put(channelId, heartInt);
        }else {
            heartInt.incrementAndGet();
        }
        log.warn("channel : {} \n heart count :{}",channelId,heartInt.get());
        return heartInt.get();
    }

    private void removeHeart(String channelId){
        heartMap.remove(channelId);
    }

    private void resetHeart(String channelId){
        AtomicInteger heartInt = heartMap.get(channelId);
        if(heartInt == null) {
            heartInt = new AtomicInteger(0);
            heartMap.put(channelId, heartInt);
        }else {
            heartInt.set(0);
        }
    }
}
