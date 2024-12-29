package com.opensource.sentinel.datasource.init;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.opensource.sentinel.datasource.apollo.ApolloDataSource;

import java.util.List;

public class ApolloDataSourceInitFunc implements InitFunc {

    @Override
    public void init() throws Exception {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>("sentinel_rules", "flowRule", "[]", source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>(){}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
