package com.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.EventListener;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/26 22:19
 * @Version 1.0
 * @motto God bless my code
 * EventLoopGroup 是对应Reactor事件循环组
 * ServerBootstrap 配置参数 启动对象（链式编程）
 *  客户端通道 ：需要使用childHandler设置
 *     设置时： 需要创建ChannelInitializer 并且声明其泛型
 *     实现初始化方法时：拿到管道 增加自定义的处理器
 */
public class NettyServer {
    public static void main(String[] args) {
        //创建两个Reactor 构建主从reactor模型
        // 用来管理channel 监听事件 是无限循环的事件组（线程池）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //服务端的引导程序/启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //设置相关参数
        serverBootstrap
                .group(bossGroup,workerGroup)
                //声明通道类型
                //      netty                  nio                 bio
                //NioServerSocketChannel<-ServerSocketChannel<-ServerSocket
                .channel(NioServerSocketChannel.class)
                //设置前面通道的处理器，使用netty提供的日志打印处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                //定义客户端连接处理器的使用
                //此方法需要参数 ChannelInitializer 通道初始化器
                // 初始化器 要处理的是客户端通道 所以泛型设置为SocketChannel
                // 此类为抽象类 需要实现其抽象方法
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //通过channel可以获取管道 pipeLine
                        // 通道代表的是 连接的角色 管道代表的是 处理业务的逻辑管理
                        //管道相当于一个链表，将不同的处理器连接起来，管理的是处理器的顺序
                        // addLast 尾插法，插入尾部的方式
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("服务器初始化完成");
        //启动并设置端口号，需要使用sync异步启动
        try {
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            //将关闭通道的方式设置为异步的
            // 阻塞中finally
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅的关闭（慢慢的关闭 给他一定的保存时间）
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
