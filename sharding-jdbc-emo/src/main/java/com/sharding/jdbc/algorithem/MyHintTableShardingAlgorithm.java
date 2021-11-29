package com.sharding.jdbc.algorithem;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Arrays;
import java.util.Collection;

/**
 * @description:
 **/
public class MyHintTableShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<Integer> shardingValue) {
        //availableTargetNames : [course_1, course_2]
        System.out.println("MyHintTableShardingAlgorithm : " + availableTargetNames);
        // hintManager.addTableShardingValue("course", 2);
        String key = shardingValue.getLogicTableName() + "_" + shardingValue.getValues().toArray()[0];
        if (availableTargetNames.contains(key)) {
            return Arrays.asList(key);
        }
        throw new UnsupportedOperationException("route " + key + " is not supported ,please check your config");
    }
}
