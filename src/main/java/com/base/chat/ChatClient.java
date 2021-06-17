package com.base.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/26 10:41
 * @Version 1.0
 * @motto God bless my code
 */
public class ChatClient {
    private SocketChannel channel;
    private Selector selector;

    public ChatClient() throws Exception {
        selector = Selector.open();
        SocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        channel = SocketChannel.open(address);

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("用户"+channel.getLocalAddress()+"上线了");
    }
    public void sendData(String msg){
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readData() throws IOException {
        int num = selector.select();
        if(num>0){
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer);

                    String msg = new String(buffer.array());
                    System.out.println(msg);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final ChatClient client = new ChatClient();
        new Thread(){
            public void run() {
                while (true){

                    try {
                        client.readData();
                        Thread.currentThread().sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            client.sendData(str);
        }
    }
}
