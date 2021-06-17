package com.base.ThreadModel;

/*
    线程模型：
        1.单线程的Reactor模型：（redis）
        2.多线程的Reactor模型：提高handler处理效率，首先handler不再负责具体的业务逻辑
                            分发给子线程处理，子线程处理完成后再将结果返回给Handler，Handler再将结果返回给客户端。
        3.主从Reactor模型：mainReactor用来接收连接事件，分发给acceptor，acceptor在处理过程中，直接将后续的读写事件
                         注册到slaveReactor中，以此来达到分流。

 */
public class Status {
    public static void main(String[] args) {
        String a = "hello";
        String b = "hello";
        System.out.println(a==b);
    }
}


