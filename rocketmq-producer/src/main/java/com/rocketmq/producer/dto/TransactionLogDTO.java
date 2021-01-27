package com.rocketmq.producer.dto;

import lombok.Data;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 13:52
 * @Description :
 * @Version :  0.0.1
 */
@Data
public class TransactionLogDTO {

    //事务ID
    private Long Id;

    //业务标识
    private String business;

    //对应业务表中的主键
    private String foreignKey;

}
