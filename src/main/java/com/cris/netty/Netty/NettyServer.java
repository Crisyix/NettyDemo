package com.cris.netty.Netty;

import com.cris.netty.Netty.handler.FirstServerHandler;
import com.cris.netty.Netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();//启动引导类

        NioEventLoopGroup boss = new NioEventLoopGroup();//boss组，负责处理接收新连接
        NioEventLoopGroup worker = new NioEventLoopGroup();//worker组，负责处理数据的读写
        serverBootstrap
                .group(boss, worker)//添加worker和boss
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)//指定io模型和类型抽象，这里对应bio的serverSocket
                .childHandler(new ChannelInitializer<NioSocketChannel>() {//定义连接的数据读写业务逻辑
                    protected void initChannel(NioSocketChannel ch) {//对应bio的socket
                       ch.pipeline().addLast(new ServerHandler());

                    }
                });
                //.childOption(ChannelOption.SO_KEEPALIVE,true);//为每条连接设置TCP相关属性
           // serverBootstrap.handler(new ServerHandler());
            bind(serverBootstrap,8000);

//        serverBootstrap.bind(8080).addListener(future -> { // bind是异步方法，返回ChannelFuture，可以对其添加监听器
//            if(future.isSuccess()){
//                System.out.println("绑定成功");
//            }
//            else
//                System.out.println("绑定失败");
//        });
    }

    public static void bind(final ServerBootstrap bootstrap,final int port){//final修饰的基本类型不能修改，引用类型不能修改引用
        bootstrap.bind(port).addListener(feature -> {
            if(feature.isSuccess()){
                System.out.println("端口[" + port + "]绑定成功!");
            }
            else{
                System.err.println("端口[" + port + "]绑定失败!");
                bind(bootstrap,port+1);
            }
        });
    }
}
