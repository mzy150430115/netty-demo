package com.base.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/24 12:09
 * @Version 1.0
 * @motto God bless my code
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //用线程池创建线程
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //设定端口号
        ServerSocket serverSocket = new ServerSocket(1234);
        //不断的轮询是否有客户端的连接
        while (true){

            //等待客户端连接，阻塞队列
            //自动增加final代表不可变，确保线程安全。
            final Socket socket = serverSocket.accept();
            threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    //服务端的处理
    private static void handler(Socket socket) throws IOException {
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());

        //接受客户端传递的数据
        byte[] bytes = new byte[1024];
        InputStream is = socket.getInputStream();
        while (true){
            int read = is.read(bytes);
            if(read==-1) break;
            System.out.println(new String(bytes,0,read));
        }
        is.close();
    }
}
