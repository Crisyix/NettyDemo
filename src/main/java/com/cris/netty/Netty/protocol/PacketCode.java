package com.cris.netty.Netty.protocol;

import com.cris.netty.Netty.Serialize.Imp.JSONSerializer;
import com.cris.netty.Netty.Serialize.Serializer;
import com.cris.netty.Netty.protocol.Packets.*;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;


//编码，数据格式为   魔数-版本-序列化算法-指令-数据长度-数据
public class PacketCode {


    private static final int MAGIC_CODE = 0x20202020;
    public static final PacketCode INSTANCE = new PacketCode();

    private  final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private  final Map<Byte, Serializer> serializerMap;

    PacketCode() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }


    public void encode(Packet packet,ByteBuf buffer){

        //序列化
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //编码
        buffer.writeInt(MAGIC_CODE);
        buffer.writeByte(packet.getVersion());
        buffer.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        buffer.writeByte(packet.getCommand());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);

    }

    public Packet decode(ByteBuf byteBuf){
        //跳过魔数
        byteBuf.skipBytes(4);
        //跳过版本号
        byteBuf.skipBytes(1);

        byte serializeAlgorithm = byteBuf.readByte();

        byte command = byteBuf.readByte();

        int length = byteBuf.readInt();

        byte[] data = new byte[length];
        byteBuf.readBytes(data);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if(requestType!=null&&serializer!=null){
            return serializer.deserialize(requestType,data);
        }
        System.out.println("code a null");
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
