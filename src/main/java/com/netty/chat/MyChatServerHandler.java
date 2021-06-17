package com.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;
import java.util.Iterator;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/28 15:13
 * @Version 1.0
 * @motto God bless my code
 */
//处理器的另一种实现方式
public class MyChatServerHandler extends SimpleChannelInboundHandler {

    //当多个通道传入Handler 我们要使用通道组
    //GlobalEventExecutor 全局事件执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //刚刚建立连接，第一个被执行的方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("[服务器] - "+ctx.channel().remoteAddress() + "连接成功\r\n");
        channelGroup.add(ctx.channel());

        //super.handlerAdded(ctx);
    }

    //当连接被断开 最后会执行的方法
    //自动将channel从channel组中移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("[服务器] - "+ctx.channel().remoteAddress() + "断开连接\r\n");
        //super.handlerRemoved(ctx);
    }

    //连接成功 此时通道是活跃的
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("Welcome to server");
        ctx.write("It is"+new Date()+"now.\r\n");
        ctx.flush();
        //super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"下线了");
       // super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel selfChannel  = ctx.channel();
        Iterator<Channel> iterator = channelGroup.iterator();
        while(iterator.hasNext()){
            Channel next = iterator.next();
            if(selfChannel!=next){
                next.writeAndFlush("[服务器] - "+selfChannel.remoteAddress() + "发送信息"+msg+"\n");
                continue;
            }
            String answer;
            if(((String)msg).length()==0){
                answer = "Please say something \r\n";
            }else{
                answer = "Did you say"+msg+"?\r\n";
            }
            next.writeAndFlush(answer);
        }
    }
}
