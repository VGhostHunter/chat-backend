package com.dhy.chat.web.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncGlobalConfig extends AsyncConfigurerSupport {

    private static final String THREAD_PREFIX = "globalAsync-";

    private final AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler;

    public AsyncGlobalConfig(AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler) {
        this.asyncUncaughtExceptionHandler = asyncUncaughtExceptionHandler;
    }

    /**
     * @link https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247503725&idx=1&sn=16a25488f6de353537da82c89a456596
     * 连接数 = ((核心数 * 2) + 有效磁盘数)
     * 公理：你需要一个小连接池，和一个充满了等待连接的线程的队列
     * @return Executor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(THREAD_PREFIX);
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncUncaughtExceptionHandler;
    }
}
