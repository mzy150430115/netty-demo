package com.base.nio;

import java.nio.CharBuffer;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/25 17:33
 * @Version 1.0
 * @motto God bless my code
 */
public class BufferedTest {
    public static void main(String[] args) {
        CharBuffer  buffer = CharBuffer.allocate(8);
        System.out.println("capacity:"+buffer.capacity());
        System.out.println("limit:"+buffer.limit());
        System.out.println("position:"+buffer.position());
        buffer.put('y');
        buffer.put('u');
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        //标记 存储当前的位置
        buffer.mark();
        System.out.println(buffer.get());
        System.out.println("position:"+buffer.position());
        //回退 回退到mark的位置
        buffer.reset();
        buffer.clear();
    }
}
