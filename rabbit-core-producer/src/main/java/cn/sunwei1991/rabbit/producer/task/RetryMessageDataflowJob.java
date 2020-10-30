package cn.sunwei1991.rabbit.producer.task;

import cn.sunwei1991.rabbit.producer.broker.RabbitBroker;
import cn.sunwei1991.rabbit.producer.constant.BrokerMessageStatus;
import cn.sunwei1991.rabbit.producer.entity.BrokerMessage;
import cn.sunwei1991.rabbit.producer.service.MessageStoreService;
import cn.sunwei1991.rabbit.task.annotaion.ElasticJobConfig;
import cn.sunwei1991.rabbit.task.annotaion.JobCoreConfiguration;
import cn.sunwei1991.rabbit.task.annotaion.LiteJobConfiguration;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Title RetryMessageDataflowJob
 * @Description
 * @Author SunWei
 * @Create 2020/10/22 8:41 上午
 */
@Slf4j
@ElasticJobConfig(
        coreConfig = @JobCoreConfiguration(
                name = "cn.sunwei1991.rabbit.producer.task.RetryMessageDataflowJob",
                cron = "0/10 * * * * ?",
                description = "可靠性投递消息补偿任务",
                shardingTotalCount = 1
        ),
        liteJobConfig = @LiteJobConfiguration(overwrite = true)

)
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    private MessageStoreService messageStoreService;

    private RabbitBroker rabbitBroker;

    public RetryMessageDataflowJob(MessageStoreService messageStoreService, RabbitBroker rabbitBroker) {
        this.messageStoreService = messageStoreService;
        this.rabbitBroker = rabbitBroker;
    }

    private static final int MAX_RETRY_COUNT = 3;


    /**
     * @Description 获取需要重试投递消息的数据
     * @Param [shardingContext]
     * @Return java.util.List<cn.sunwei1991.rabbit.producer.entity.BrokerMessage>
     */
    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> brokerMessages = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("--------@@抓取数据集合,数量：{}",brokerMessages.size());
        return brokerMessages;
    }

    /**
     * @Description 补偿投递消息
     * @Param [shardingContext, list]
     * @Return void
     */
    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> list) {
        list.forEach(brokerMessage -> {
            //判断消息投递次数是否已经超过最大努力次数
            if(MAX_RETRY_COUNT <= brokerMessage.getTryCount()){
                messageStoreService.failure(brokerMessage.getMessageId());
                log.warn("消息最大努力重试最终失败，消息ID：{}", brokerMessage.getMessageId());
            }else {
                //重试 更新 try count字段
                messageStoreService.updatedTryCount(brokerMessage.getMessageId());
                rabbitBroker.reliantSend(brokerMessage.getMessage());
            }
        });
    }
}
