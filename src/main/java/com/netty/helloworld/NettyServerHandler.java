package com.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/27 12:31
 * @Version 1.0
 * @motto God bless my code
 */
//自定义handler方式之一
    //继承ChannelInboundHandlerAdapter
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //通道被启用，刚刚建立连接要使用的方法。
    //业务使用中，往往发送一些欢迎消息

    //数据读取的方法
    //当客户端发送时，读事件触发的方法
    //Object msg 就是传送的消息数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //netty的缓冲区叫ByteBuf 对ByteBuffer的封装
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("client msg:"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("client is from" + ctx.channel().remoteAddress());
        //super.channelRead(ctx, msg);
    }

    //数据读取完成的方法
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //数据处理还是使用ByteBuf Unpooled是提供 在ByteBuf和String之间方便转换的工具类
        //Unpooled的常用方法 copiedBuffer 直接处理String返回ByteBuf
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client how are you?",CharsetUtil.UTF_8));
        //super.channelReadComplete(ctx);
    }

    /**
     *
     * @param ctx 通道处理器的上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //写数据时，可以调用writeAndFlush 直接写入字符串
        // 底层：获取通道-创建缓冲区-写入数据-缓冲区写入通道
        System.out.println("channelActive done");
        ctx.writeAndFlush("welcome to netty server");
        //super.channelActive(ctx);
    }


}
