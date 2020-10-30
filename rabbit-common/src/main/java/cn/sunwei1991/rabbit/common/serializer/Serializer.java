package cn.sunwei1991.rabbit.common.serializer;

/**
 * @Title Serializer
 * @Description 序列化反序列化接口
 * @Author SunWei
 * @Create 2020/10/21 1:51 下午
 */
public interface Serializer {

    byte[] serializerRaw(Object data);

    String serializer(Object data);

    <T> T deserializer(String content);

    <T> T deserializer(byte[] content);
}
