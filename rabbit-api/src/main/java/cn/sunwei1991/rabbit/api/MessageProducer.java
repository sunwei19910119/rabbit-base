package cn.sunwei1991.rabbit.api;

import cn.sunwei1991.rabbit.exception.MessageRunTimeException;

import java.util.List;

/**
 * @Title MessageProducer
 * @Description 可靠性投递消息提供者开放接口
 * @Author SunWei
 * @Create 2020/10/21 10:29 上午
 */
public interface MessageProducer {

    /**
     * @Description 消息的发送附带SendCallback回调执行响应的业务逻辑处理
     * @Param [message, sendCallback]
     * @Return void
     */
    void send(Message message,SendCallback sendCallback);

    /**
     * @Description 消息发送
     * @Param [message]
     * @Return void
    */
    void send(Message message) throws MessageRunTimeException;

    /**
     * @Description 消息批量发送
     * @Param [messages]
     * @Return void
    */
    void send(List<Message> messages) throws MessageRunTimeException;
}
