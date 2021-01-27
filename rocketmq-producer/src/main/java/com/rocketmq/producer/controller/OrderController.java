package com.rocketmq.producer.controller;

import com.rocketmq.producer.dto.OrderDTO;
import com.rocketmq.producer.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 13:47
 * @Description :
 * @Version :  0.0.1
 */
@Controller
@RequestMapping("/api")
@ResponseBody
public class OrderController {

    @Autowired
    OrderService orderService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create_order")
    public void createOrder(@RequestBody OrderDTO order) throws MQClientException {
        logger.info("接收到订单数据：{}", order.getCommodityCode());
        orderService.createOrder(order);
    }

}
