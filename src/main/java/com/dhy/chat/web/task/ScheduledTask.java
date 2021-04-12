package com.dhy.chat.web.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class ScheduledTask {

    /**
     * 如果这里是异步的则每次都会从线程池调度一个线程去执行
     * 如果没有线程则进入队列 所以如果任务执行时间太长 最多同时可能有多个任务同时执行
     * @throws InterruptedException
     */
    @Async
    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask1() throws InterruptedException {
        int i = 1/0;
        System.out.println(Thread.currentThread().getName() + "---scheduledTask1 " + new Date());
    }

    @Async
    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask2 " + new Date());
    }
}
