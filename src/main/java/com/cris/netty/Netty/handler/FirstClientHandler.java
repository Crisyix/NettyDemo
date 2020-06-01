package com.cris.netty.Netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务端"+"数据"+byteBuf.toString(StandardCharsets.UTF_8));

        System.out.println(new Date()+"客户端写数据");

        ByteBuf byteBuf2 = getBuf(ctx);
        ctx.channel().writeAndFlush(byteBuf2);
        byteBuf.release();
    }


    private ByteBuf getBuf(ChannelHandlerContext ctx){

        ByteBuf byteBuf = ctx.alloc().buffer();
        byte[] data = "hello netty server".getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(data);
        return byteBuf;
    }
}
