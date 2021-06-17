package com.base.socket.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/25 22:23
 * @Version 1.0
 * @motto God bless my code
 */
public class NioSelectorServer {
    public static void main(String[] args) throws Exception {
        //创建一个服务端的通道，调用OPEN 区别于普通的BIO创建
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //绑定IP和端口
        SocketAddress address = new InetSocketAddress("127.0.0.1",4321);
        serverChannel.socket().bind(address);
        //将channel设置为同步非阻塞的
        serverChannel.configureBlocking(false);
        //打开一个选择器
        Selector selector = Selector.open();
        //将通道注册进选择器中 声明选择器监听的事件 第一个被注册的事件往往是接收连接
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        //通过选择器管理通道 需要感知 被监听的通道是否有操作被触发
        selector.select();
        while (true){
            int ready = selector.select();
            if(ready==0) continue;
            //进一步获取要执行的操作
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //取出操作后直接移除，避免重复处理。
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                }
                else if(key.isWritable()){
                    //通过key的channel方法   获取事件对应的通道
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(128);
                    writeBuffer.put("hello from 4321".getBytes());
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);

                    //再把事件注册进来
                    key.interestOps(SelectionKey.OP_READ);

                }
                else if(key.isWritable()){
                    //处理OP_READ的事件
                    SocketChannel socketChannel = (SocketChannel) key.channel();

                    ByteBuffer readBuffer = ByteBuffer.allocate(128);
                    socketChannel.read(readBuffer);
                    readBuffer.flip();

                    StringBuffer stringBuffer = new StringBuffer();
                    while (readBuffer.hasRemaining()){
                        stringBuffer.append((char)readBuffer.get());
                    }
                    System.out.println("client data:"+stringBuffer.toString());

                }
            }
        }

    }
}
