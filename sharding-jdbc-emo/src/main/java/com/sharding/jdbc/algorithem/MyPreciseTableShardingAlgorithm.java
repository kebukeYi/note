package com.sharding.jdbc.algorithem;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.math.BigInteger;
import java.util.Collection;

/**
 * @description: standard标准分片策略下的 自定义 表 精准查询策略
 **/
public class MyPreciseTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    //select * from course where cid = ? or cid in (?,?)

    //Collection<String> availableTargetNames : 所有可能的表
    //PreciseShardingValue<Long> shardingValue:  分片key 关键字
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        String logicTableName = shardingValue.getLogicTableName();
        String cid = shardingValue.getColumnName();
        Long cidValue = shardingValue.getValue();
        //实现 course_$->{cid%2+1)
        BigInteger shardingValueB = BigInteger.valueOf(cidValue);
        BigInteger resB = (shardingValueB.mod(new BigInteger("2"))).add(new BigInteger("1"));
        String key = logicTableName + "_" + resB;
        if (availableTargetNames.contains(key)) {
            return key;
        }
        //couse_1, course_2
        throw new UnsupportedOperationException("route " + key + " is not supported ,please check your config");
    }
}
