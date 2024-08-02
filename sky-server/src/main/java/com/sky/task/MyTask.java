package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/31 18:31
 * @Description:自定义定时任务类
 **/
//@Component
@Slf4j
public class MyTask {
    //定时任务 每5s触发一次  s min h day month year
    @Scheduled(cron = "0/5 * * * * ?")
    public void executeTask(){
        log.info("定时任务开始：{}",new Date());
    }
}
