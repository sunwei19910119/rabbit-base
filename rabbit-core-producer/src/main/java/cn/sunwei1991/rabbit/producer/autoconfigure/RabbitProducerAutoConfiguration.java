package cn.sunwei1991.rabbit.producer.autoconfigure;

import cn.sunwei1991.rabbit.producer.broker.ProducerClient;
import cn.sunwei1991.rabbit.producer.broker.RabbitBroker;
import cn.sunwei1991.rabbit.producer.broker.RabbitBrokerImpl;
import cn.sunwei1991.rabbit.producer.broker.RabbitTemplateContainer;
import cn.sunwei1991.rabbit.producer.config.database.RabbitProduceMyBatisConfiguration;
import cn.sunwei1991.rabbit.producer.mapper.BrokerMessageMapper;
import cn.sunwei1991.rabbit.producer.service.MessageStoreService;
import cn.sunwei1991.rabbit.producer.task.RetryMessageDataflowJob;
import cn.sunwei1991.rabbit.task.annotaion.EnableElasticJob;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title RabbitProducerAutoConfiguration
 * @Description 自动装配
 * @Author SunWei
 * @Create 2020/10/21 4:33 下午
 */
@EnableElasticJob
@Slf4j
@Configuration
@AutoConfigureAfter(value = {RabbitProduceMyBatisConfiguration.class})
public class RabbitProducerAutoConfiguration {

    public RabbitProducerAutoConfiguration() {
        log.info("Initializing RabbitProducerAutoConfiguration");
    }

    @Autowired
    private SqlSessionTemplate rabbitProducerSqlSessionTemplate;


    @Bean
    public BrokerMessageMapper brokerMessageMapper(){
        return rabbitProducerSqlSessionTemplate.getMapper(BrokerMessageMapper.class);
    }

    @Bean
    public MessageStoreService messageStoreService(){
        return new MessageStoreService(brokerMessageMapper());
    }

    @Bean
    public RabbitTemplateContainer rabbitTemplateContainer(){
        return new RabbitTemplateContainer(messageStoreService());
    }

    @Bean
    public RabbitBroker rabbitBroker(){
        return new RabbitBrokerImpl(rabbitTemplateContainer(), messageStoreService());
    }

    @Bean
    public ProducerClient producerClient(){
        return new ProducerClient(rabbitBroker());
    }

    @Bean
    public RetryMessageDataflowJob retryMessageDataflowJob(){
        return new RetryMessageDataflowJob(messageStoreService(), rabbitBroker());
    }
}
