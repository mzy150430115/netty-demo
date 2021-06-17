package com.base.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/24 13:24
 * @Version 1.0
 * @motto God bless my code
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",1234);
        OutputStream os = socket.getOutputStream();
        String message = "hello socket";

        os.write(message.getBytes());

        os.close();
        socket.close();
    }
}
