package com.cris.netty.Netty.protocol.Packets;

import com.cris.netty.Netty.protocol.*;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet{

    private String response;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
