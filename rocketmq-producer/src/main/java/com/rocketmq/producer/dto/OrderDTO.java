package com.rocketmq.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 13:01
 * @Description :
 * @Version :  0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {


    private Integer commodityCode;
    private Long transactionId;
    private String business;
    private String orderNo;

}
