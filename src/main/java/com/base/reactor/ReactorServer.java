package com.base.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ReactorServer {

    // 对应角色为Reactor 反应器 、通知器、 监听器
    private Selector selector;
    private ServerSocketChannel serverChannel;

    public ReactorServer() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();

            serverChannel.configureBlocking(false);

            SocketAddress address = new InetSocketAddress(8888);
            serverChannel.socket().bind(address);

            // 注册 连接事件的同时   声明一个Acceptor和事件绑定
            SelectionKey key = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 创建一个Acceptor  放入selectionkey中
            Acceptor acceptor = new Acceptor(serverChannel, selector);
            // Acceptor作为一个附加对象进行绑定
            key.attach(acceptor);


            while (true) {
                int num = selector.select();
                if (num == 0) continue;

                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> iterator = set.iterator();

                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    // 拿到之前存储的附加对象
                    //   如果事件是接收事件   分发给绑定的acceptor
                    //   如果事件是读写事件   分发给绑定的handler
                    Runnable runnable = (Runnable)selectionKey.attachment();
                    runnable.run();

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
