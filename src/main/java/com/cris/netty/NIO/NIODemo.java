package com.cris.netty.NIO;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIODemo {
    public static void main(String[] args) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(5);//缓冲区
        RandomAccessFile  f = new RandomAccessFile("t.txt","rw");
        FileChannel fileChannel = f.getChannel();
        int len;

        buf.put((byte) 72);
        buf.flip();
       // System.out.println(fileChannel.size()); //字符长度
        fileChannel.write(buf);//直接写入文件

        while((len = fileChannel.read(buf))!=-1){//从channel读出到缓冲区
            buf.flip();//buf转换为读模式，limit=position, position=0，
            //fileChannel.write(buf);//将缓冲区写入channel,
           // System.out.println("d");
            while (buf.hasRemaining())
                System.out.print((char)buf.get());
            buf.clear();//buf转换为写模式，position=0,limit = cap
        }
        fileChannel.close();
        f.close();
    }
}
