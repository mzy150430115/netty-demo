package com.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/28 23:31
 * @Version 1.0
 * @motto God bless my code
 */
public class MyChatClient {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //默认创建线程数量 = CPU处理器数量*2
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new MyChatClientInitializer());

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8899).sync();
            future.channel().closeFuture().sync();

            //通过键盘获取到要发送的数据
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                String msg = br.readLine();
                future.channel().writeAndFlush(msg+"\r\n");

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
