package com.minzheng.blog.config

import com.minzheng.blog.logger
import lombok.extern.slf4j.Slf4j
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy

/**
 * Async 配置
 */
@Slf4j
@Configuration
class AsyncTaskConfig : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        executor.setThreadNamePrefix("async-anno-task-")
        executor.corePoolSize = 2
        executor.maxPoolSize = 8
        executor.keepAliveSeconds = 5
        executor.setQueueCapacity(100)
        executor.setRejectedExecutionHandler(AbortPolicy())
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setAwaitTerminationSeconds(60)
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return AsyncUncaughtExceptionHandler { ex: Throwable, method: Method, params: Array<Any?>? ->
            logger().error(
                "Async Task Has Some Error: {}, {}, {}",
                ex.message,
                method.declaringClass.name + "." + method.name,
                Arrays.toString(params)
            )
        }
    }
}