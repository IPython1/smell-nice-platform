package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/31 18:58
 * @Description: 定时任务类 定时处理订单状态
 **/
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    //处理超时订单的方法 每分钟执行一次
    @Scheduled(cron = "0 * * * * ?")
//    @Scheduled(cron = "0/10 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        //select * from orders where status = 1 and order_time < ?(当前时间-15min)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                //更新订单状态为已取消
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }
    //处理一直处于派送中订单的方法 每天凌晨一点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder() {
        log.info("定时处理派送中订单：{}", LocalDateTime.now());
        //select * from orders where status = 3 and order_time < ?(当前时间-60min)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusMinutes(60));
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                //更新订单状态为已完成
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
