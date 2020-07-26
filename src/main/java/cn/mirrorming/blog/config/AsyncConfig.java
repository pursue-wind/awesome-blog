package cn.mirrorming.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mireal Chan
 */
@EnableAsync
public class AsyncConfig {

    /**
     * 异步任务执行线程池
     */
    @Bean(name = "asyncExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(50);
        //队列最大长度
        executor.setQueueCapacity(1000);
        //线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(300);
        //自定义线程名字前缀
        executor.setThreadNamePrefix("AsyncExecutorThread-");
        //线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
