package com.rocketmq.producer.service.lmpl;

import com.rocketmq.producer.mapper.TransactionLogMapper;
import com.rocketmq.producer.service.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 13:06
 * @Description :
 * @Version :  0.0.1
 */
@Service
public class TransactionLogServicelmpl implements TransactionLogService {

    @Autowired
    TransactionLogMapper transactionLogMapper;

    @Override
    public Integer get(String transactionId) {
        return null;
    }
}
