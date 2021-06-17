package com.base.nio;
public class NIOTest {
    /*
     1.BIO：BlockingIO 同步阻塞（区别于NIO BIO是面向流的，NIO将文件或文件中的一块区域映射到内存中，可以像访问内存一样的访问）
     2.NIO：New IO/Non-Blocking IO 同步非阻塞
     3.AIO：Asynchronous IO 异步非阻塞

     同步和异步，关注的是消息通知的机制
     阻塞和非阻塞，关注的是等待消息过程中的状态

     NIO核心三要素：
        通道：channel
            FileChannel,
            Pipe.SinkChannel, 线程之间通信的管道
            Pipe.SourceChannel,
            ServerSocketChannel,
            SocketChannel,
            DatagramChannel
        缓冲区：buffered
             capacity,总体容量大小
             limit,存储容量大小，是可读写和不可读写的界限
             position已读容量大小，已读和未读区域的界限
           使用原理：1.初始化 给定总容量 position=0 limit=capacity
                    2.当使用put方法存入数据的时，通过position来记录存储的容量变化，position不断后移，直到存储结束
                    3.写完成需要调用flip方法刷新，limit=position，position=0，保证limit记录的是可读写区域的大小，已读写部分重置为空。
                    4.读数据直到读取完成，clear方法，position=0 limit=capacity
        选择器；Selector
           三个元素：
                Selector选择器，（被注册的通道集合，以及他们的状态）
                    本质上是一种监听器，监听通道是否有我们关心的操作产生，操作对应的事件（连接，接受，读/写）
                    使用SelectionKey代表具体的事件，在确保通道是可选择的情况下，通道注册到选择器中，
                    此时Selector维护的是，通道与事件之间的关联关系。

                SelectableChannel可选择的通道，
                    是一个抽象类，提供了通道可选择需要实现的API。FileChannel就是不可选择的，Socket相关的通道都是可选择的。
                    一个通道可以被注册到多个服务器上吗？可以的。
                    多个通道可以被注册到一个服务器上，但一个通道只能在一个选择器中注册一次
                SelectionKey选择键
                    封装了要监听的事件，连接，接收，读，写。
                    一方面，selector关心通道要处理哪些事件。
                    另一方面，当事件触发时，通道要执行处理哪些事件。
           使用方法：
            1.获取open方法，获取通道，将通道设为非阻塞的。
            2.同过open方法，获取选择器，将通道注册到选择器中，伴随设置通道要处理的事件（OPEN_ACCEPT）
            3.轮询选择器，当前是否有要处理的操作 select()>0?
                    如果有，要获取待处理这些操作的集合（set）每个操作都是selectionKey，进行遍历。
                        遍历到SelectionKey时，判断对应哪种操作，不同的操作一般会设置不同的处理方式。
                        如果OP_ACCEPT,接受客户端通道，进行注册，后续处理的事件，如OP_WRITE
                        如果OP_WRITE,通过key的方法获取通道本身，读取数据并继续监听事件，如OP_READ

     */

}
