package com.orange.sqlexecutor.util.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/3
 * Time:下午4:20
 **/
@Configuration
@EnableAsync //自定义线程池的配置类，并在类上添加@EnableAsync 注解，然后在需要异步的方法上使用@Async("线程池名称") 该方法就可以异步执行了
public class ExecutorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public Executor threadPoolTaskExecutor() {
        logger.info("threadPoolTaskExecutor创建完成");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(50);
        //配置最大线程数
        executor.setMaxPoolSize(100);
        //线程空闲时间
        executor.setKeepAliveSeconds(60);
        //配置队列大小
        executor.setQueueCapacity(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("threadPoolTaskExecutor-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public ExecutorService cachedThreadPool() {
        logger.info("cachedThreadPool创建完成");

        ExecutorService executor = Executors.newCachedThreadPool();
        return executor;
    }
}
