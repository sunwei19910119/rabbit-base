package cn.sunwei1991.rabbit.producer.broker;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.api.MessageProducer;
import cn.sunwei1991.rabbit.api.MessageType;
import cn.sunwei1991.rabbit.api.SendCallback;
import cn.sunwei1991.rabbit.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * @Title ProducerClient
 * @Description 发送消息的实际实现类
 * @Author SunWei
 * @Create 2020/10/22 9:17 上午
 */
public class ProducerClient implements MessageProducer {

    private RabbitBroker rabbitBroker;

    public ProducerClient(RabbitBroker rabbitBroker){
        this.rabbitBroker = rabbitBroker;
    }

    @Override
    public void send(Message message, SendCallback sendCallback) {
        rabbitBroker.sendCallback(message,sendCallback);
    }

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        // 根据消息的可靠性级别进行投递消息
        String messageType = message.getMessageType();
        switch (messageType){
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    /**
     * @Description 批量消息发送
     * @Param [messages]
     * @Return void
    */
    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();
    }
}
