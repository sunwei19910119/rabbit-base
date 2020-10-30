package cn.sunwei1991.rabbit.producer.broker;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.api.MessageType;
import cn.sunwei1991.rabbit.common.convert.GenericMessageConverter;
import cn.sunwei1991.rabbit.common.convert.RabbitMessageConverter;
import cn.sunwei1991.rabbit.common.serializer.Serializer;
import cn.sunwei1991.rabbit.common.serializer.SerializerFactory;
import cn.sunwei1991.rabbit.common.serializer.impl.JacksonSerializerFactory;
import cn.sunwei1991.rabbit.exception.MessageRunTimeException;
import cn.sunwei1991.rabbit.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Title RabbitTemplateContainer
 * @Description 池化封装
 *          池化优点:
 *          1.提高发送的效率
 *          2.可以根据不同的需求制定化不同的RabbitTemplate。比如每一个topic都有自己的RoutingKey规则
 * @Author SunWei
 * @Create 2020/10/22 9:25 上午
 */
@Slf4j
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    /** rabbitTemplate存储，key 是topic，value 是该topic的RabbitTemplate存储 */
    private Map<String,RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

    private Splitter splitter = Splitter.on("#");

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    @Autowired
    private ConnectionFactory connectionFactory;

    private MessageStoreService messageStoreService;

    public RabbitTemplateContainer(MessageStoreService messageStoreService){
        this.messageStoreService = messageStoreService;
    }

    public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if(rabbitTemplate != null){
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());

        //添加对于自定义Message对象序列化反序列化和converter对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        //装饰 gmc
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newRabbitTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        if(!MessageType.RAPID.equals(messageType)){
            newRabbitTemplate.setConfirmCallback(this);
        }
        rabbitMap.put(topic,newRabbitTemplate);
        return rabbitMap.get(topic);
    }

    /**
     * @Description confirm消息和reliant消息都会让broker去调用confirm
     * @Param [correlationData, b, s]
     * @Return void
    */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        Long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);

        if(ack){
            //当 Broker 返回 ACK 成功时，并且 reliant 类型的消息，更新数据库记录数据状态为 SEND_OK
            if(MessageType.RELIANT.endsWith(messageType)){
                messageStoreService.success(messageId);
            }
            // else do nothing
            log.info("send message is ok, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }else {
            log.error("send message if Fail, confirm messageId: {}, sendTime: {}, cause: {}", messageId, sendTime, cause);
        }
    }
}
