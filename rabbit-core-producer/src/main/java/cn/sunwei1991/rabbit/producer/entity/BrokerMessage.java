package cn.sunwei1991.rabbit.producer.entity;

import cn.sunwei1991.rabbit.api.Message;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title BrokerMessage
 * @Description 消息记录表实体映射
 * @Author SunWei
 * @Create 2020/10/21 4:52 下午
 */
@Data
public class BrokerMessage implements Serializable {

    private static final long serialVersionUID = 7653601643919051924L;

    private String messageId;

    private Message message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;
}
