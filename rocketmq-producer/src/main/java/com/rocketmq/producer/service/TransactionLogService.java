package com.rocketmq.producer.service;

public interface TransactionLogService {
    Integer get(String transactionId);
}
