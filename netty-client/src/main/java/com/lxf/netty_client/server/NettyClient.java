package com.lxf.netty_client.server;

import com.lxf.netty_client.handle.NettyClientHandler;
import com.lxf.netty_client.initializer.NettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;

/**
 * @Classname NettyClient
 * @Description
 * @Date 2023/9/14 16:40
 * @Author lxf
 */
@Component
@Slf4j
public class NettyClient {

    @Autowired
    private NettyClientInitializer nettyClientInitializer;

    @PostConstruct
    public void start(){

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(nettyClientInitializer);
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8899).sync();
            //得到 channel
            Channel channel = channelFuture.channel();
            log.info("========" + channel.localAddress() + "========");
            log.info("channel : " + channel.isActive());
            //客户端需要输入信息， 创建一个扫描器
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNextLine()) {
//                String msg = scanner.nextLine();
//                //通过 channel 发送到服务器端
//                channel.writeAndFlush(msg);
//            }
//            for (int i = 0; i < 10; i++) {
//                channel.writeAndFlush("hello,sun!");
//            }
            channel.writeAndFlush("hello,gozi");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.warn("finally server shutdown ready");
           // group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer(){
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ByteArrayEncoder());
                            pipeline.addLast(new ByteArrayDecoder());
                            pipeline.addLast();
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8899).sync();
            //得到 channel
            Channel channel = channelFuture.channel();
            log.info("========" + channel.localAddress() + "========");
            log.info("channel : " + channel.isActive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
