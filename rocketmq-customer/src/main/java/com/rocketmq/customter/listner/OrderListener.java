package com.rocketmq.customter.listner;

import com.alibaba.fastjson.JSONObject;
import com.rocketmq.customter.dto.OrderDTO;
import com.rocketmq.customter.service.TpointsService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
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
        logger.info("消费者线程监听到消息");
        try {
            for (MessageExt message : list) {
                logger.info("开始处理订单数据，准备增加积分....");
                //这里虽然获得消息了  也提交了 offset 给mq了 ;但是系统突然宕机了 那就还是还是消费失败了 下发红包失败
                OrderDTO order = JSONObject.parseObject(message.getBody(), OrderDTO.class);
                //但是这里我们要警惕一点，就是我们不能在代码中对消息进行异步的处理，如下错误的示范，我们开启了一个子线程去处理这批消息，
                // 然后启动线程之后，就直接返回ConsumeConcurrentlyStatus.CONSUME_SUCCESS状态了
                pointsService.increasePoints(order);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            logger.error("处理消费者数据发生异常 {}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
