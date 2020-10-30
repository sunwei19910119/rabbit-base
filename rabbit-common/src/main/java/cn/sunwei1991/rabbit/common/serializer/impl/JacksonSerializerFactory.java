package cn.sunwei1991.rabbit.common.serializer.impl;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.common.serializer.Serializer;
import cn.sunwei1991.rabbit.common.serializer.SerializerFactory;

/**
 * @Title JacksonSerializerFactory
 * @Description
 * @Author SunWei
 * @Create 2020/10/21 1:52 下午
 */
public class JacksonSerializerFactory implements SerializerFactory {

    public static final JacksonSerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
