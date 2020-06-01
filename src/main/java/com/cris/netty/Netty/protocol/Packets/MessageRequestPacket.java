package com.cris.netty.Netty.protocol.Packets;

import com.cris.netty.Netty.protocol.*;
import lombok.Data;

@Data
public class MessageRequestPacket extends Packet{

    private String message;


    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
