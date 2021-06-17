package com.base.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {
    public static void main(String[] args) throws IOException {
        File file = new File("nio.txt");
        if(!file.exists()) file.createNewFile();
        FileOutputStream os = new FileOutputStream(file);
        FileChannel channel = os.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = "hello nio";
        buffer.put(str.getBytes());

        //写完成
        buffer.flip();
        channel.write(buffer);

        channel.close();
        os.close();
    }
}
