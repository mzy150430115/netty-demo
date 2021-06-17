package com.base.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/26 8:21
 * @Version 1.0
 * @motto God bless my code
 */
public class ChatServer {
    //服务端通道
    private ServerSocketChannel channel;
    //多路复用选择器
    private Selector selector;

    public ChatServer() throws IOException {
        channel = ServerSocketChannel.open();
        selector = Selector.open();
        SocketAddress address = new InetSocketAddress(6666);
        channel.socket().bind(address);
        channel.configureBlocking(false);
        //注册通道可接收事件
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }
    public void listenClient() throws Exception{
        System.out.println("服务端启动监听");
        while (true){
            //询问选择器是否有事件要处理
            int num = selector.select();
            if(num==0) continue;
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                //识别key所对应的事件种类
                if(key.isAcceptable()){
                    SocketChannel clientChannel = channel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println("用户"+clientChannel
                            .socket()
                            .getRemoteSocketAddress()+"上线了");
                    continue;
                }
                if(key.isReadable()){
                    readData(key);
                }
            }
        }
    }
    public void readData(SelectionKey key) throws IOException {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int num = channel.read(buffer);
            if(num>0) {
                String msg = new String(buffer.array());
                msg = msg+"from"+channel.socket().getRemoteSocketAddress();
                System.out.println("msg:" + msg);
                //再将数据广播给其他客户端（不包括发送数据的客户端自身）
                sendToOther(msg, channel);
            }
        }catch (Exception e){
            //没有获取到通道 用户下线了
            System.out.println("用户"+channel.socket().getRemoteSocketAddress()+"下线了");
            //取消注册关系
            key.cancel();
            channel.close();

        }

    }
    private void sendToOther(String msg,SocketChannel selfChannel) throws IOException {
        //找到所有的通道（找到所有的在线用户）
        Set<SelectionKey> set = selector.selectedKeys();
        for (SelectionKey selectionKey : set) {
            Channel channel = selectionKey.channel();
            if(channel instanceof SocketChannel && channel!=selfChannel){
                SocketChannel socketChannel = (SocketChannel) channel;
                //wrap方法 直接将array数据 存放到缓存区
                ByteBuffer buffer  = ByteBuffer.wrap(msg.getBytes());
                socketChannel.write(buffer);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        ChatServer server = new ChatServer();
        server.listenClient();
    }
}
