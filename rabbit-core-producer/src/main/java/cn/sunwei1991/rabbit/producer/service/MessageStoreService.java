package cn.sunwei1991.rabbit.producer.service;

import cn.sunwei1991.rabbit.producer.constant.BrokerMessageStatus;
import cn.sunwei1991.rabbit.producer.entity.BrokerMessage;
import cn.sunwei1991.rabbit.producer.mapper.BrokerMessageMapper;

import java.util.Date;
import java.util.List;

/**
 * @Title MessageStoreService
 * @Description
 * @Author SunWei
 * @Create 2020/10/21 5:11 下午
 */
public class MessageStoreService {
    private BrokerMessageMapper brokerMessageMapper;


    public MessageStoreService(BrokerMessageMapper brokerMessageMapper) {
        this.brokerMessageMapper = brokerMessageMapper;
    }

    public int insert(BrokerMessage brokerMessage){
        return brokerMessageMapper.insert(brokerMessage);
    }

    public BrokerMessage selectByMessageId(String messageId){
        return brokerMessageMapper.selectByPrimaryKey(messageId);
    }

    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    public void failure(String messageId){
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }

    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus status) {
        return brokerMessageMapper.queryBrokerMessageStatus4Timeout(status.getCode());
    }

    public int updatedTryCount(String messageId){
        return brokerMessageMapper.update4TryCount(messageId, new Date());
    }
}
