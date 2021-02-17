package com.rocketmq.customter.service.lmpl;

import cn.hutool.core.lang.Snowflake;
import com.rocketmq.customter.dto.OrderDTO;
import com.rocketmq.customter.dto.TpointsDTO;
import com.rocketmq.customter.mapper.TpointsMapper;
import com.rocketmq.customter.service.TpointsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 14:29
 * @Description :
 * @Version :  0.0.1
 */
@Service(value = "TpointsServcicelmpl")
public class TpointsServcicelmpl implements TpointsService {

    @Autowired
    TpointsMapper pointsMapper;

    Snowflake snowflake = new Snowflake(1, 1);

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void increasePoints(OrderDTO order) {
        //入库之前先查询，实现幂等
        if (pointsMapper.getByOrderNo(order.getOrderNo()) > 0) {
            logger.info("积分添加完成，订单已处理。{}", order.getOrderNo());
        } else {
            TpointsDTO points = new TpointsDTO();
            points.setUserid(order.getUserid());
            points.setOrderno(order.getOrderNo());
            Double amount = order.getAmount();
            points.setPoints(Long.valueOf(amount.intValue() * 10));
            points.setRemarks("商品消费共【" + order.getAmount() + "】元，获得积分" + points.getPoints());
            pointsMapper.insert(points);
            logger.info("已为订单号码{}增加积分。", points.getOrderno());
        }
    }
}
