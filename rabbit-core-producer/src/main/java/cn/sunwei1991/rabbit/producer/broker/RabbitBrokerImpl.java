package cn.sunwei1991.rabbit.producer.broker;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.api.MessageType;
import cn.sunwei1991.rabbit.api.SendCallback;
import cn.sunwei1991.rabbit.producer.constant.BrokerMessageConst;
import cn.sunwei1991.rabbit.producer.constant.BrokerMessageStatus;
import cn.sunwei1991.rabbit.producer.entity.BrokerMessage;
import cn.sunwei1991.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Title RabbitBrokerImpl
 * @Description 实现发送不同类型的消息
 * @Author SunWei
 * @Create 2020/10/22 9:22 上午
 */
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    private RabbitTemplateContainer rabbitTemplateContainer;

    private MessageStoreService messageStoreService;

    public RabbitBrokerImpl(RabbitTemplateContainer rabbitTemplateContainer, MessageStoreService messageStoreService) {
        this.rabbitTemplateContainer = rabbitTemplateContainer;
        this.messageStoreService = messageStoreService;
    }

    @Override
    public void sendCallback(Message message, SendCallback sendCallback) {
        message.setMessageType(MessageType.CONFIRM);
        if (null != sendCallback){
            sendCallbackMessage(message, sendCallback);
        }else {
            sendKernel(message);
        }
    }

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage brokerMessage = messageStoreService.selectByMessageId(message.getMessageId());
        if (null == brokerMessage){
            // 1. 待发送的消息落库记录
            Date now = new Date();
            brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            // try_count 在第一次发送时不需要被赋值 默认0;
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            messageStoreService.insert(brokerMessage);
        }

        // 2. 发送消息到MQ
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        List<Message> messageList = MessageHolder.clear();
        messageList.forEach(message -> {
            MessageHolderAsyncQueue.submit(() ->{
                CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s"
                        , message.getMessageId()
                        , System.currentTimeMillis()
                        , message.getMessageType()));
                String topic = message.getTopic();
                String routingKey = message.getRoutingKey();
                RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
                rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
                log.info("#RabbitBrokerImpl.sendMessages# send to rabbitmq messageId: {}", message.getMessageId());

            });
        });
    }

    /**
     * @Description 发送消息的核心方法，异步线程池进行发送消息
     * @Param [message]
     * @Return void
    */
    private void sendKernel(Message message){
        AsyncBaseQueue.submit((Runnable) () -> {
            CorrelationData correlationData = new CorrelationData(String.format("%s%s%s",
                    message.getMessageId(),
                    System.currentTimeMillis(),
                    message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq messageId: {}", message.getMessageId());
        });
    }

    private void sendCallbackMessage(Message message,SendCallback sendCallback){
        CallbackAsyncQueue.submit((Runnable) ()-> {
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s"
                    , message.getMessageId()
                    , System.currentTimeMillis()
                    , message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendCallbackMessage# send to rabbitmq messageId: {}", message.getMessageId());

            try {
                if (correlationData.getFuture().get().isAck()){
                    sendCallback.onSuccess();
                }else {
                    sendCallback.onFailure();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
