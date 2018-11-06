package nsl.sam.scheduler

import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

object ScheduledExecutor {

    val log = LoggerFactory.getLogger(this::class.java.name)!!

    var shootdownHookRegistered = false

    private var executor: ScheduledExecutorService? = null

    private fun getScheduledExecutor(): ScheduledExecutorService {
        log.debug("getSchduledExecutor()")
        if(executor == null || executor!!.isShutdown) {
            executor = Executors.newSingleThreadScheduledExecutor()
        }
        return executor!!
    }

    fun addTask(task: Runnable, periodMilliseconds: Long) {
        synchronized(this) {
            log.debug("addTask()")

            getScheduledExecutor().scheduleAtFixedRate(task, periodMilliseconds, periodMilliseconds, TimeUnit.MILLISECONDS)

            if(!shootdownHookRegistered) {
                log.info("${this::class.simpleName} is registering JVM shutdown hook")
                Runtime.getRuntime().addShutdownHook(thread(start = false) {
                    log.info("${this::class.simpleName} shutdown hook fired up")
                    if(executor != null && !executor!!.isShutdown) {
                        log.info("${this::class.simpleName} is shutting down its internal executor")
                        executor!!.shutdownNow()
                    }

                })
                shootdownHookRegistered = true
            }
        }
    }

    fun shutdownNow() {
        synchronized(this) {
            log.debug("shutdownNow()")
            getScheduledExecutor().shutdownNow()
        }

    }

    fun awaitTermination() {
        synchronized(this) {
            log.debug("awaitTermination()")
            getScheduledExecutor().awaitTermination(3, TimeUnit.MINUTES)
        }
    }

}