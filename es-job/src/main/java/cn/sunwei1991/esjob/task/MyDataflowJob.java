package cn.sunwei1991.esjob.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import cn.sunwei1991.esjob.entity.Foo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MyDataflowJob implements DataflowJob<Foo> {

    @Override
    public List<Foo> fetchData(ShardingContext shardingContext) {
        log.info("======== 抓取数据集合");
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Foo> list) {
        log.info("======== 处理数据集合");
    }
}
