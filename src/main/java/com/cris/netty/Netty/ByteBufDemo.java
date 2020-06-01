package com.cris.netty.Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class ByteBufDemo {
    public static void main(String[] args) {
        Deque<Integer> deque = new ArrayDeque<>();


        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();//默认cap = 256,maxCap = Integer.MAX_VALUE;
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.ioBuffer();



        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(9,100);//初始容量和最大容量

        byteBuf.writeBytes(new byte[]{1,2,3,4,5});
        print("writeBytes(new byte[]{1,2,3,4，5})",byteBuf);

        byteBuf.writeInt(3);//写完int，写指针+4,到达初始容量，不可写
        print("write(3)",byteBuf);

        byteBuf.writeByte(6);//不可写下继续写入，则扩容，从64开始，之后每次扩容2倍，同时要小于最大容量，容量相应改变
        print("write([6])",byteBuf);

        //set不影响读写指针
        byteBuf.setByte(byteBuf.writableBytes() + 1, 0);
        print("setByte()", byteBuf);


        //get方法不改变读写指针，
        System.out.println("getByte(3) return: " + byteBuf.getByte(3));
        System.out.println("getShort(3) return: " + byteBuf.getShort(3));
        System.out.println("getInt(3) return: " + byteBuf.getInt(3));
        print("getByte()", byteBuf);

        //read方法改变读指针
        byte[] bts = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bts);
        print("readBytes",byteBuf);



    }
    private static void print(String action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buffer.capacity());
        System.out.println("maxCapacity(): " + buffer.maxCapacity());
        System.out.println("readerIndex(): " + buffer.readerIndex());
        System.out.println("readableBytes(): " + buffer.readableBytes());
        System.out.println("isReadable(): " + buffer.isReadable());
        System.out.println("writerIndex(): " + buffer.writerIndex());
        System.out.println("writableBytes(): " + buffer.writableBytes());
        System.out.println("isWritable(): " + buffer.isWritable());
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();
    }
}
