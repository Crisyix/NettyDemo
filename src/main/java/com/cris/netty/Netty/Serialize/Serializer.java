package com.cris.netty.Netty.Serialize;


import com.cris.netty.Netty.Serialize.Imp.JSONSerializer;

public interface Serializer {

    //序列化算法
    byte  getSerializerAlgorithm();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz,byte[] bytes);

    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();
}
