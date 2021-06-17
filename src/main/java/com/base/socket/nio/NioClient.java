package com.base.socket.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/25 20:54
 * @Version 1.0
 * @motto God bless my code
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();

        SocketAddress address = new InetSocketAddress("127.0.0.1",4321);
        socketChannel.connect(address);

        //先写后读
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        writeBuffer.put("hello server from client".getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        socketChannel.read(readBuffer);
        readBuffer.flip();

        StringBuffer stringBuffer = new StringBuffer();
        while (readBuffer.hasRemaining()){
            stringBuffer.append((char)readBuffer.get());
        }
        System.out.println("server data:"+stringBuffer.toString());

        socketChannel.close();

    }
}
