package com.cris.netty.Netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {//接受数据
        ByteBuf byteBuf = (ByteBuf) msg;//需要强转？

        System.out.println("收到客户端"+"数据"+byteBuf.toString(StandardCharsets.UTF_8));
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) {//发送数据
        System.out.println(new Date()+"服务端写数据");
        ByteBuf byteBuf = getBuf(ctx);

        ctx.channel().writeAndFlush(byteBuf);

        byteBuf.markReaderIndex();//一组读指针标记获取与重置
        byteBuf.resetReaderIndex();
    }

    private ByteBuf getBuf(ChannelHandlerContext ctx){

        //获取二进制抽象ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();

        byte[] data = "hello netty client".getBytes(StandardCharsets.UTF_8);
        //填充数据到byteBuf中
        byteBuf.writeBytes(data);

        return byteBuf;
    }
}
