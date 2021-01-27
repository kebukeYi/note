package com.rocketmq.producer.mapper;

import com.rocketmq.producer.dto.TransactionLogDTO;

public interface TransactionLogMapper {
    Integer insert(TransactionLogDTO log);
}
