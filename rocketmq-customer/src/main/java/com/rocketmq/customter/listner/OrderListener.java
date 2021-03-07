package com.rocketmq.customter.listner;

import com.alibaba.fastjson.JSONObject;
import com.rocketmq.customter.dto.OrderDTO;
import com.rocketmq.customter.service.TpointsService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 14:31
 * @Description :
 * @Version :  0.0.1
 */
@Component
public class OrderListener implements MessageListenerConcurrently {

    @Autowired
    @Qualifier(value = "TpointsServcicelmpl")
    TpointsService pointsService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        logger.info("消费者线程监听到消息。");
        try {
            for (MessageExt message : list) {
                logger.info("开始处理订单数据，准备增加积分....");
                OrderDTO order = JSONObject.parseObject(message.getBody(), OrderDTO.class);
                pointsService.increasePoints(order);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            logger.error("处理消费者数据发生异常 {}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    public ConsumeOrderlyStatus consumeOrderlyMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        logger.info("消费者线程监听到消息。");
        try {
            for (MessageExt message : list) {
                logger.info("开始对一些顺序消息进行处理");
                OrderDTO order = JSONObject.parseObject(message.getBody(), OrderDTO.class);
                pointsService.increasePoints(order);
            }
            return ConsumeOrderlyStatus.SUCCESS;
        } catch (Exception e) {
            logger.error("处理消费者数据发生异常 {}", e);
            //稍微等待一会再发送数据
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
    }


    public ConsumeConcurrentlyStatus consumeTagMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        logger.info("消费者线程监听到消息");
        try {
            for (MessageExt message : list) {
                logger.info("开始处理订单数据，准备增加积分....");
                OrderDTO order = JSONObject.parseObject(message.getBody(), OrderDTO.class);
                pointsService.increasePoints(order);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            logger.error("处理消费者数据发生异常 {}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }


    /*
      消息处理，第3次处理失败后，发送邮件通知人工介入
      @param message
      @return
     */
    private boolean processor(MessageExt message) {
        String body = new String(message.getBody());
        try {
            logger.info("消息处理....{}", body);
            int k = 1 / 0;
            return true;
        } catch (Exception e) {
            if (message.getReconsumeTimes() >= 3) {
                logger.error("消息重试已达最大次数，将通知业务人员排查问题。{}", message.getMsgId());
                sendMail(message);
                return true;
            }
            return false;
        }
    }

    private void sendMail(MessageExt message) {
    }
}