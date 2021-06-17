package com.netty.websocket;

import com.netty.http.MyHttpInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/29 12:10
 * @Version 1.0
 * @motto God bless my code
 */
public class WebSocketServer {
    public static void main(String[] args) {
        //可以自定义线程的数量
        //可以自定义线程的数量
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //默认创建线程数量 = CPU处理器数量*2
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                //当连接被阻塞时，BackLog表示 阻塞队列的长度
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new WebSocketInitializer());
        try {
            ChannelFuture future = serverBootstrap.bind(7777).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
