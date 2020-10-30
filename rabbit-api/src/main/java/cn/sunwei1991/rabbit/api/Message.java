package cn.sunwei1991.rabbit.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Message
 * @Author： SunWei
 * @Create： 2020/10/19 10:40 上午
 * @Description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 4910615243449458329L;

    /** 消息唯一ID **/
    private String messageId;

    /** 消息的主题 **/
    private String topic;

    /** 消息的路由规则 **/
    private String routingKey = "";

    /** 消息的附加属性 **/
    private Map<String, Object> attributes = new HashMap<>();

    /** 延迟消息的参数配置 PS:需要RabbitMQ安装延迟插件 **/
    private int delayMills;

    /** 消息类型 默认confirm消息 **/
    private String messageType = MessageType.CONFIRM;

}
