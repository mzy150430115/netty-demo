package com.mzy.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/6/17 15:21
 * @Version 1.0
 * @motto God bless my code
 */
// channelRead0()是对原有的channelRead()方法的封装 增加自动释放资源的逻辑
public class FileHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String url = msg.uri();
        url = URLDecoder.decode(url,"UTF-8");
        String path = System.getProperty("user.dir") + File.separator + url;
        System.out.println("path=="+path);
        //遍历所有文件 返回
        String data = fileList(path);

        //如果是文件 可以拦截 不返回


        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");

        ByteBuf byteBuf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        //增加监听方法 监听异步关闭的事件
        ctx.writeAndFlush(response).addListeners(ChannelFutureListener.CLOSE);
    }


    //对文件夹进行遍历
    public String fileList(String path){
        File file = new File(path);

        StringBuilder builder = new StringBuilder();
        builder.append("<html><head><title>");
        builder.append("http文件服务");
        builder.append("</title></head><body>\r\n");
        builder.append("<h3>");
        builder.append(path).append("目录");
        builder.append("</h3>");
        builder.append("<ul><li> 连接：<a href=\" .. /\"> .. </a></li> \r\n");
        for (File f : file.listFiles()) {
            String name = f.getName();
            builder.append("<li>连接：<a href=\"");
            builder.append(name);
            builder.append("\">");
            builder.append(name);
            builder.append("</a></li>\r\n");
        }
        builder.append("</ul></body></html>\r\n");
        return builder.toString();
    }
}
