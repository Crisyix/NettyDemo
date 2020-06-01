package com.cris.netty.IO;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class IOClient {
    public static void main(String[] args) {
        //while(true){
        for(int i =0;i<5;++i){
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 2380);//创建socket连接
               while (true) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world"+Thread.currentThread().getName()).getBytes());//向此socket连接输出数据
                        Thread.sleep(2000);//每隔两秒
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        }
    }
   // }
}
