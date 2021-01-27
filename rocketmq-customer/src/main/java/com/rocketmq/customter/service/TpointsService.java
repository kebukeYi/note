package com.rocketmq.customter.service;

import com.rocketmq.customter.dto.OrderDTO;

public interface TpointsService {
    void increasePoints(OrderDTO order);
}
