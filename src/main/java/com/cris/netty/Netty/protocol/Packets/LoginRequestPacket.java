package com.cris.netty.Netty.protocol.Packets;

import lombok.Data;

import com.cris.netty.Netty.protocol.*;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
