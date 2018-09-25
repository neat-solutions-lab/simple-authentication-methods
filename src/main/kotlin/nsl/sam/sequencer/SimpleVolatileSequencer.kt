package nsl.sam.sequencer

import java.util.concurrent.atomic.AtomicLong

open class SimpleVolatileSequencer(initValue: Long = 0, private val delta: Long = 1) {

    private val currentValue = AtomicLong(initValue)

    fun getNextValue(): Long {
        return currentValue.addAndGet(delta)
    }
}