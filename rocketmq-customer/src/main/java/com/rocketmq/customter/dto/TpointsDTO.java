package com.rocketmq.customter.dto;

import lombok.Data;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 14:27
 * @Description :
 * @Version :  0.0.1
 */
@Data
public class TpointsDTO {
    private Long id;
    private Long userid;
    private String orderno;
    private Long points;
    private String remarks;
}
