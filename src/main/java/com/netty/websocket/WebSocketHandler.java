package com.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/29 12:16
 * @Version 1.0
 * @motto God bless my code
 */
//泛型 代表处理数据的单位
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("msg："+msg.text());
        Channel channel = ctx.channel();
        TextWebSocketFrame response = new TextWebSocketFrame("hello client from webSocket server");
        channel.writeAndFlush(response);

    }
}
