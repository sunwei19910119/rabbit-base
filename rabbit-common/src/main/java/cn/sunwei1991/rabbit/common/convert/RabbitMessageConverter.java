package cn.sunwei1991.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @Title RabbitMessageConverter
 * @Description 基于GenericMessageConverter的代理,使用了装饰者模式
 * @Author SunWei
 * @Create 2020/10/21 2:36 下午
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    /** 默认过期时间 **/
    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        //加入自己的业务逻辑
        // 设置消息过期时间
//        messageProperties.setExpiration(defaultExpire);
        cn.sunwei1991.rabbit.api.Message message = (cn.sunwei1991.rabbit.api.Message) o;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(o, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        cn.sunwei1991.rabbit.api.Message msg = (cn.sunwei1991.rabbit.api.Message) this.delegate.fromMessage(message);
        return msg;
    }
}
