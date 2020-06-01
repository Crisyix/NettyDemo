package com.cris.netty.Netty.handler;

import com.cris.netty.Netty.protocol.Packets.LoginRequestPacket;
import com.cris.netty.Netty.protocol.Packets.LoginResponsePacket;
import com.cris.netty.Netty.protocol.Packets.MessageResponsePacket;
import com.cris.netty.Netty.protocol.Packets.Packet;
import com.cris.netty.Netty.protocol.PacketCode;
import com.cris.netty.Netty.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端登录---");

        //创建登录对象
        LoginRequestPacket lrp = new LoginRequestPacket();
        lrp.setUserId(UUID.randomUUID().toString());
        lrp.setUsername("cris");
        lrp.setPassword("wyx123");

        //编码
        ByteBuf buf = ctx.alloc().buffer();
        PacketCode.INSTANCE.encode(lrp,buf);

        //
        ctx.channel().writeAndFlush(buf);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端响应");
        ByteBuf buf = (ByteBuf) msg;
        Packet packet = PacketCode.INSTANCE.decode(buf);
        if(packet instanceof LoginResponsePacket){
            LoginResponsePacket resp = (LoginResponsePacket) packet;
            if(resp.isSuccess()){
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println("登陆成功");
            }
            else {
                System.out.println(resp.getReason());
            }
        }
        else if (packet instanceof MessageResponsePacket){
            MessageResponsePacket mresp = (MessageResponsePacket) packet;
            System.out.println(new Date() +"收到服务器消息"+ mresp.getResponse());

        }
    }
}
