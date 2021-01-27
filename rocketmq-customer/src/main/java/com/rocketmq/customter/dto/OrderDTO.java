package com.rocketmq.customter.dto;

import lombok.Data;

@Data
public class OrderDTO {
    //主键id
    private Long id;
    //用户id
    private Long userid;
    //订单code
    private Integer commodityCode;
    //事务id
    private Long transactionId;
    //订单号码
    private String orderNo;
    //订单金额
    private Double amount;
}
