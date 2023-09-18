package com.lxf.netty_client.initializer;

import com.lxf.netty_client.handle.NettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Classname NettyClientInitializer
 * @Description 连接监控
 * @Date 2023/9/14 16:05
 * @Author lxf
 */
@Component
@Slf4j
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {


    //连接注册，创建成功，会被调用
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
        pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
//        pipeline.addLast(new HttpServerCodec());
//        pipeline.addLast(new ChunkedWriteHandler());
//        // 几乎在netty中的编程，都会使用到此hanler
//        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        pipeline.addLast(new NettyClientHandler());
    }
}
