package com.rocketmq.customter.mapper;

import com.rocketmq.customter.dto.TpointsDTO;

public interface TpointsMapper {

    Integer getByOrderNo(String orderNo);

    Integer insert(TpointsDTO points);
}
