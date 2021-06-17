package com.base.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

public class MultiHandler implements Runnable {

    private SelectionKey key;

    private State state;

    private ExecutorService pool;

    public MultiHandler(SelectionKey key) {
        this.key = key;
        this.state = State.READ;
    }


    private void read() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            int num = channel.read(buffer);
            String msg = new String(buffer.array());

            key.interestOps(SelectionKey.OP_WRITE);
            this.state = State.WRITE;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void write() {

        ByteBuffer buffer = ByteBuffer.wrap("hello".getBytes());
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(buffer);

            key.interestOps(SelectionKey.OP_READ);
            this.state = State.READ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
            switch (state) {
                case READ:
                    // 将耗时操作放在线程池中执行
                    pool.execute(() -> {
                        read();
                    });
                    break;

                case WRITE:
                    write();
                    break;
            }

    }

    private enum State {
        READ, WRITE
    }
}
