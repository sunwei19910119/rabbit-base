package cn.sunwei1991.rabbit.producer.broker;

import cn.sunwei1991.rabbit.api.Message;
import cn.sunwei1991.rabbit.api.SendCallback;

/**
 * @Title RabbitBroker
 * @Description 定义发送不同类型消息接口
 * @Author SunWei
 * @Create 2020/10/22 9:18 上午
 */
public interface RabbitBroker {

    /**
     * @Description 消息的发送附带SendCallback函数
     * 特点：异步发送，有ACK，回调函数
     * @Param [message, sendCallback]
     * @Return void
    */
    void sendCallback(Message message, SendCallback sendCallback);

    /**
     * @Description 迅速消息
     * 特点：异步发送，无ACK
     * @Param [message]
     * @Return void
    */
    void rapidSend(Message message);

    /**
     * @Description 确认消息
     * 特点：异步发送，有ACK
     * @Param [message]
     * @Return void
    */
    void confirmSend(Message message);

    /**
     * @Description 可靠性消息
     * 特点：消息落库，重试机制，有ACK
     * @Param [message]
     * @Return void
    */
    void reliantSend(Message message);

    /**
     * @Description 批量发送
     * 特点：基于ThreadLocal实现的批量发送
     * @Param []
     * @Return void
    */
    void sendMessages();
}


