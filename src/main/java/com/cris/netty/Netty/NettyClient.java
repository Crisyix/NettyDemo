package com.cris.netty.Netty;

import com.cris.netty.Netty.handler.ClientHandler;
import com.cris.netty.Netty.protocol.PacketCode;
import com.cris.netty.Netty.protocol.Packets.MessageRequestPacket;
import com.cris.netty.Netty.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static int MAX_RETRY = 5;
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class);//指定io模型
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline().addLast(new ClientHandler());
            }
        });


        connect(bootstrap,"127.0.0.1", 8000,MAX_RETRY);

        //Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

//        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(2000);
//        }

    }
    public static void connect(Bootstrap bootstrap,String host,int port,int retry){
        bootstrap.connect(host,port).addListener(future->{
            if(future.isSuccess()){
                System.out.println("连接成功");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            }
            else if(retry==0){
                System.err.println("重连次数已完");
            }
            else{
                int order = MAX_RETRY-retry+1;
                int delay = order<<1;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(()->connect(bootstrap, host, port, retry-1),
                        delay, TimeUnit.SECONDS);//schedule为执行定时任务
            }
        });
    }

    public static void startConsoleThread(Channel channel){
        new Thread(()->{
            while (!Thread.interrupted()){
                if(LoginUtil.hasLogin(channel)){
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setMessage(line);
                    ByteBuf byteBuf = channel.alloc().ioBuffer();
                    PacketCode.INSTANCE.encode(messageRequestPacket,byteBuf);
                    channel.writeAndFlush(byteBuf);
                    System.out.println(line+"done");
                }
            }
        }).start();
    }
}
