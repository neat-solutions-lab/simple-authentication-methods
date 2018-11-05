package nsl.sam.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ScheduledExecutor {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun addTask(task: Runnable, periodMilliseconds: Long) {
        executor.scheduleAtFixedRate(task, periodMilliseconds, periodMilliseconds, TimeUnit.MILLISECONDS)
    }

    fun shutdownNow() {
        executor.shutdownNow()
    }

    fun awaitTermination() {
        executor.awaitTermination(3, TimeUnit.MINUTES)
    }

}