package com.rocketmq.customter.service.lmpl;

import com.rocketmq.customter.dto.OrderDTO;
import com.rocketmq.customter.service.TpointsService;
import org.springframework.stereotype.Service;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-29 15:09
 * @Description :
 * @Version :  0.0.1
 */
@Service(value = "PointServicelmpl")
public class PointServicelmpl implements TpointsService {
    @Override
    public void increasePoints(OrderDTO order) {

    }
}
