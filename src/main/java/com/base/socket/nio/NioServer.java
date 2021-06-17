package com.base.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/25 20:26
 * @Version 1.0
 * @motto God bless my code
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建一个服务端的通道，调用OPEN 区别于普通的BIO创建
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //绑定IP和端口
        SocketAddress address = new InetSocketAddress("127.0.0.1",4321);
        serverChannel.socket().bind(address);
        //接收客户端的连接
        SocketChannel socketChannel = serverChannel.accept();

        //数据处理
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put("hello,client".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        socketChannel.read(readBuffer);

        readBuffer.flip();
        StringBuffer stringBuffer = new StringBuffer();
        while (readBuffer.hasRemaining()){
            stringBuffer.append((char)readBuffer.get());
        }
        System.out.println("client data:"+stringBuffer.toString());
        socketChannel.close();
        serverChannel.close();
    }
}
