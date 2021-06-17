package com.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import javax.xml.ws.spi.http.HttpHandler;

//泛型需要设置为FullHttpRequest
    //筛选msg为此类型的消息处理
public class MyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //DefaultFullHttpResponse是一个默认的完整的HTTP响应
        //需要设定 版本，响应码，数据
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1
                ,HttpResponseStatus.OK
                , Unpooled.wrappedBuffer("hello http netty demo".getBytes())
        );
        //设置请求头HttpHeaders来接收
        //设置请求头/响应头字段时，可以使用HttpHeaderNames
        //设置字段值时，可以使用HttpHeaderValues;
        //设置包的大小时，调用readableBytes方法
        HttpHeaders headers = response.headers();
        headers.add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.TEXT_PLAIN+";charset=UTF-8");
        headers.add(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        ctx.write(response);

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
