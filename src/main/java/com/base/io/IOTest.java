package com.base.io;

import java.io.*;

/**
 * 输入流 InputStream Reader
 * 输出流 OutputStream Writer
 * 缓存流 BufferedInputStream BufferedOutputStream
 */
public class IOTest {
    public static void main(String[] args) throws Exception {
        File file = new File("io.text");
        //写入
        String str = "hello,io";
//        OutputStream os = new FileOutputStream(file);
//        os.write(str.getBytes());
        Writer writer  = new FileWriter(file);
        writer.write(str);
        writer.close();
        BufferedInputStream bi = new BufferedInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });

        //读出
        InputStream is = new FileInputStream(file);
        byte[] byteArr = new byte[(int) file.length()];
        int read = is.read(byteArr);
        System.out.println("读取数据的大小："
                +read+"，数据内容为："+new String(byteArr));

        is.close();
    }
    //拷贝文件内容
    public static void copy(String srcName,String destName) throws IOException {
        File src = new File(srcName);
        File dest = new File(destName);
        if(!dest.exists()){
            dest.createNewFile();
        }
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);

        byte[] byteArr = new byte[1024];
        int size = 0;
        //读到文件尾的时候，没有数据的时候返回-1
        while ((size = is.read(byteArr) )!=-1){
            os.write(byteArr,0,size);
        }
        os.flush();
        os.close();

        is.close();
    }
}
