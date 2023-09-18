package com.lxf.netty_server.server;

import com.lxf.netty_server.initializer.NettyServer2Initializer;
import com.lxf.netty_server.initializer.NettyServerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Classname NettyServer
 * @Description
 * @Date 2023/9/14 16:03
 * @Author lxf
 */
@Component
@Slf4j
public class NettyServer {
    @Autowired
    private NettyServerInitializer nettyServerInitializer;

    @Value("${netty.server.port:8899}")
    private int port;

    @PostConstruct
    public void start() throws InterruptedException {
        //创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //服务端进行启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用NIO模式，初始化器等等
            serverBootstrap.group(bossGroup, workerGroup)
                            //使用NioServerSocketChannel作为服务器的通道实现
                            .channel(NioServerSocketChannel.class)
                            // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                            // 多个客户端同时来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            //创建通道初始化对象，设置初始化参数
                            .childHandler(nettyServerInitializer);
            //绑定一个端口并且同步, 生成了一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
            //启动服务器(并绑定端口)，bind是异步操作，sync方法是等待异步操作执行完毕
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("【netty服务器已经启动......】");
            //给ChannelFuture注册监听器，监听我们关心的事件
            //对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.warn("finally server shutdown ready");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.warn("finally server shutdown finish");
        }
    }


    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyServer2Initializer());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8899).sync();
            //得到 channel
            Channel channel = channelFuture.channel();
            log.info("========" + channel.localAddress() + "========");
            log.info("channel : " + channel.isActive());
            channel.writeAndFlush("我是main方法");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
           // group.shutdownGracefully();
        }
    }

}
