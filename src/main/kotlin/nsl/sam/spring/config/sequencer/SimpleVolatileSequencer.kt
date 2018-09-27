package nsl.sam.spring.config.sequencer

import java.util.concurrent.atomic.AtomicLong

open class SimpleVolatileSequencer(initValue: Long = 0, private val delta: Long = 1) {

    companion object {

        @Volatile
        var instance: SimpleVolatileSequencer? = null

        @Synchronized
        fun getSingleton(initValue: Long = 1, delta: Long = 1): SimpleVolatileSequencer {
            if (instance == null)
                instance = SimpleVolatileSequencer(initValue, delta)
            return instance!!
        }
    }

    private val currentValue = AtomicLong(initValue)

    fun getNextValue(): Long {
        return currentValue.addAndGet(delta)
    }
}