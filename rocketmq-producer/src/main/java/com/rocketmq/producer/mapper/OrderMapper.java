package com.rocketmq.producer.mapper;

import com.rocketmq.producer.dto.OrderDTO;

public interface OrderMapper {

    Integer createOrder(OrderDTO order);



}
