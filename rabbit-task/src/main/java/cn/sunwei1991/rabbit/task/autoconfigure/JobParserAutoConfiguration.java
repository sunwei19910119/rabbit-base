package cn.sunwei1991.rabbit.task.autoconfigure;

import cn.sunwei1991.rabbit.task.parser.ElasticJobConfigParser;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title JobParserAutoConfiguration
 * @Description
 * @Author SunWei
 * @Create 2020/10/23 2:08 下午
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "elastic-job.zookeeper", name = {"serverLists", "namespace"}, matchIfMissing = false)
@EnableConfigurationProperties(ElasticJobZookeeperProperties.class)
public class JobParserAutoConfiguration {

    /**
     * 初始化zookeeper
     * @param elasticJobZookeeperProperties
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(ElasticJobZookeeperProperties elasticJobZookeeperProperties){

        ZookeeperConfiguration configuration = new ZookeeperConfiguration(elasticJobZookeeperProperties.getServerLists()
                , elasticJobZookeeperProperties.getNamespace());
        configuration.setBaseSleepTimeMilliseconds(elasticJobZookeeperProperties.getBaseSleepTimeMilliseconds());
        configuration.setMaxSleepTimeMilliseconds(elasticJobZookeeperProperties.getMaxSleepTimeMilliseconds());
        configuration.setConnectionTimeoutMilliseconds(elasticJobZookeeperProperties.getConnectionTimeoutMilliseconds());
        configuration.setSessionTimeoutMilliseconds(elasticJobZookeeperProperties.getSessionTimeoutMilliseconds());
        configuration.setMaxRetries(elasticJobZookeeperProperties.getMaxRetries());
        configuration.setDigest(elasticJobZookeeperProperties.getDigest());

        return new ZookeeperRegistryCenter(configuration);
    }

    /**
     * 初始化自定义注解解析器
     * @param elasticJobZookeeperProperties
     * @param registryCenter
     * @return
     */
    @Bean
    public ElasticJobConfigParser elasticJobConfigParser(ElasticJobZookeeperProperties elasticJobZookeeperProperties,
                                                         ZookeeperRegistryCenter registryCenter){
        return new ElasticJobConfigParser(elasticJobZookeeperProperties, registryCenter);
    }
}
