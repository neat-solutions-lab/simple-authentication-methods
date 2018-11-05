package nsl.sam.scheduler

import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object ScheduledExecutor {

    val log = LoggerFactory.getLogger(this::class.java.name)!!

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