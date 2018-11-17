package nsl.sam.core.config.sequencer

import java.util.concurrent.atomic.AtomicLong

open class SimpleSequencer(initValue: Long = 0, private val delta: Long = 1) {

    companion object {

        var instance: SimpleSequencer? = null

        @Synchronized
        fun getSingleton(initValue: Long = 0, delta: Long = 1): SimpleSequencer {
            if (instance == null)
                instance = SimpleSequencer(initValue, delta)
            return instance!!
        }
    }

    private val currentValue = AtomicLong(initValue)

    fun getNextValue(): Long {
        return currentValue.addAndGet(delta)
    }
}