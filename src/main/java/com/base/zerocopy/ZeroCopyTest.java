package com.base.zerocopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
            传统的拷贝方式：四次
                                磁盘-拷贝-操作系统内核缓冲区
                                -拷贝-应用程序缓存buffer中
                                -拷贝-Socket网络缓冲区（操作系统的内核缓冲区）
                                -拷贝-网卡
           在操作系统中进行的拷贝（第二次，第三次）叫做CPU拷贝
           连接磁盘或网卡等硬件拷贝（第一次，第四次）叫做DMA拷贝
           零拷贝是基于操作系统层面的优化方式（以下基于linux系统）
           1.MMap = memory mapping 内存映射
                                磁盘-拷贝-操作系统内核缓冲区         内存映射到应用程序缓存buffer中
                                -拷贝-Socket网络缓冲区（操作系统的内核缓冲区）
                                -拷贝-网卡
           2.sendfile（linux2.1内核支持）三次
                                磁盘-拷贝-操作系统内核缓冲区（不拷贝全部的数据，只记录数据的位置和长度）
                                -拷贝-Socket网络缓冲区（操作系统的内核缓冲区）
                                -拷贝-网卡
           3.sendfile with scatter/gather copy（批量sendfile） 两次
                                磁盘-拷贝-操作系统内核缓冲区（不拷贝全部的数据，只记录数据的位置和长度）
                                -pipe管道-Socket网络缓冲区（操作系统的内核缓冲区）
                                -拷贝-网卡
           4.splice（linux2.6内核支持）
         */
public class ZeroCopyTest {
    public static void main(String[] args) throws Exception{
        //copy("nio.txt","nio_new.txt");
        copy2("nio.txt","nio_new.txt");
    }
    public static void copy(String sourceName,String destName) throws Exception{
        File  source = new File(sourceName);
        File dest = new File(destName);
        if(!dest.exists()) dest.createNewFile();

        FileInputStream fis = new FileInputStream(source);
        FileChannel inChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel outChannel = fos.getChannel();
        //ByteBuffer子类 对应于mmap内存映射的拷贝方式（相对于传统方式快很多）
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY,0,source.length());
        outChannel.write(buffer);
        buffer.clear();

        inChannel.close();
        fis.close();
        inChannel.close();

        outChannel.close();
        fos.close();
        outChannel.close();
    }
    public static void copy2(String sourceName,String destName) throws Exception{
        File  source = new File(sourceName);
        File dest = new File(destName);
        if(!dest.exists()) dest.createNewFile();

        FileInputStream fis = new FileInputStream(source);
        FileChannel inChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel outChannel = fos.getChannel();
        //通过这个transferTo方法直接从A通道-B通道（不引入Buffer 直接调用 ）
        inChannel.transferTo(0,inChannel.size(),outChannel);
        inChannel.close();
        fis.close();
        outChannel.close();
        fos.close();

    }
}
