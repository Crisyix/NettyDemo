package com.cris.netty.Netty.handler;

import com.cris.netty.Netty.protocol.Packets.*;
import com.cris.netty.Netty.protocol.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Packet packet = PacketCode.INSTANCE.decode(buf);
        System.out.println(packet.getCommand());
        if(packet instanceof LoginRequestPacket){
            LoginRequestPacket req = (LoginRequestPacket) packet;
            //登录响应
            LoginResponsePacket resp =  new LoginResponsePacket();

            //验证有效用户
            if(valid(req)){
                resp.setSuccess(true);
                System.out.println("有效");
            }
            else{
                resp.setSuccess(false);
                resp.setReason("用户名或密码错误");
            }

            ByteBuf respBuf = ctx.alloc().buffer();
            PacketCode.INSTANCE.encode(resp,respBuf);
            ctx.channel().writeAndFlush(respBuf);
        }
        else if(packet instanceof MessageRequestPacket){
            MessageRequestPacket mrp = (MessageRequestPacket) packet;
            System.out.println(new Date()+"：收到客户端消息："+mrp.getMessage());

            MessageResponsePacket mresp = new MessageResponsePacket();
            mresp.setResponse("服务器回复："+mrp.getMessage());
            ByteBuf buf1 = ctx.alloc().buffer();
            PacketCode.INSTANCE.encode(mresp,buf1);
            ctx.channel().writeAndFlush(buf1);
        }
    }

    private boolean valid(LoginRequestPacket lrp){
        return true;
    }
}
