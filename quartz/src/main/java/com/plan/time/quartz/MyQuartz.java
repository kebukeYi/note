package com.plan.time.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 16:15
 * @description:
 * @question:
 * @link:
 **/
public class MyQuartz {

    private static Logger log = LoggerFactory.getLogger(MyQuartz.class);


    public static void main(String[] args) throws SchedulerException {
        //1.创建一个JobDetail
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withDescription("调用JobDemo")
                .withIdentity("Job's name", "Job's Group")
                .build();
        log.info("描述任务：{}" + jobDetail.getDescription());

        //2.创建一个trigger触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .withDescription("创建一个Trigger触发规则")
                .startAt(new Date())
                .withIdentity("Trigger's Name", "Trigger's Group")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(10, 5))
                .build();

        //3.创建一个调度器，也就是一个Quartz容器
        //声明一个scheduler的工厂schedulerFactory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        //通过schedulerFactory来实例化一个Scheduler
        Scheduler scheduler = schedulerFactory.getScheduler();
        //将Job和Trigger注册到scheduler容器中
        scheduler.scheduleJob(jobDetail, trigger);

        //4.启动容器
        log.info("JOB开始启动");
        scheduler.start();

    }
}
 
