package cn.sunwei1991.rabbit.common.convert;

import cn.sunwei1991.rabbit.common.serializer.Serializer;
import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @Title GenericMessageConverter
 * @Description 将自定义的Message转换为org.springframework.amqp.core.Message
 * @Author SunWei
 * @Create 2020/10/21 1:47 下午
 */
public class GenericMessageConverter implements MessageConverter {

    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes = this.serializer.serializerRaw(o);
        Message message = new Message(bytes,messageProperties);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.serializer.deserializer(message.getBody());
    }
}
