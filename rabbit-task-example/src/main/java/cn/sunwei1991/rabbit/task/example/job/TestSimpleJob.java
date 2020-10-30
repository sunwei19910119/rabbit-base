package cn.sunwei1991.rabbit.task.example.job;

import cn.sunwei1991.rabbit.task.annotaion.ElasticJobConfig;
import cn.sunwei1991.rabbit.task.annotaion.JobCoreConfiguration;
import cn.sunwei1991.rabbit.task.annotaion.ListenerConfiguration;
import cn.sunwei1991.rabbit.task.annotaion.LiteJobConfiguration;
import cn.sunwei1991.rabbit.task.example.listener.SimpleJobListener;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Title TestSimpleJob
 * @Description
 * @Author SunWei
 * @Create 2020/10/27 1:38 下午
 */
@ElasticJobConfig(
        coreConfig = @JobCoreConfiguration(name = "testSimpleJob", cron = "0/5 * * * * ?"
                , shardingTotalCount = 2, shardingItemParameters = "0=beijing,1=shanghai"
                , jobParameter = "source1=public,source2=private", failover = true
                , description = "test job"),
        liteJobConfig = @LiteJobConfiguration(monitorPort = 8889),
        listenerConfig = @ListenerConfiguration(clazz = SimpleJobListener.class)
)
@Component
@Slf4j
public class TestSimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("============== TestSimpleJob");
    }
}
