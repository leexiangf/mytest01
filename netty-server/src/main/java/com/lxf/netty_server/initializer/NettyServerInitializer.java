package com.lxf.netty_server.initializer;

import com.lxf.netty_server.handle.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @Classname NettyServerInitializer
 * @Description 连接监控
 * @Date 2023/9/14 16:05
 * @Author lxf
 */
@Component
@Slf4j
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    //连接注册，创建成功，会被调用
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("==================netty报告==================");
        log.info("信息：有一客户端链接到本服务端");
        log.info("IP:{}", ch.remoteAddress().getAddress());
        log.info("Port:{}", ch.remoteAddress().getPort());
        log.info("通道id:{}", ch.id().toString());
        log.info("==================netty报告完毕==================");
        ChannelPipeline pipeline = ch.pipeline();

        //向pipeline加入解码器
        pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
        //向pipeline加入编码器
        pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
        // websocket 基于http协议，所以要有http编解码器
//        pipeline.addLast(new HttpServerCodec());
//        pipeline.addLast(new ChunkedWriteHandler());
//        // 几乎在netty中的编程，都会使用到此hanler
//        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
//        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //定义读写空闲时间
        pipeline.addLast(new IdleStateHandler(30, 30,30, TimeUnit.SECONDS));
        //注册拦截器
        pipeline.addLast(nettyServerHandler);
    }
}
