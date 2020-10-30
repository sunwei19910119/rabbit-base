package cn.sunwei1991.rabbit.task.example.listener;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title SimpleJobListener
 * @Description ElasticJob自定义监听器
 * @Author SunWei
 * @Create 2020/10/27 1:39 下午
 */
@Slf4j
public class SimpleJobListener implements ElasticJobListener {
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        log.info("----------- 执行任务之前{}", JSON.toJSONString(shardingContexts, true));
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info("----------- 执行任务之后{}", JSON.toJSONString(shardingContexts, true));
    }
}
