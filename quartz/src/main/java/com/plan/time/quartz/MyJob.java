package com.plan.time.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 16:14
 * @description:
 * @question:
 * @link:
 **/
public class MyJob implements Job {

    private Logger log = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("我也不知道这个啥时候执行，反正这个是Job 的实现类");
    }
}
 
