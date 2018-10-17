package nsl.sam.changes

import nsl.sam.logger.logger
import java.time.Instant

abstract class AbstractChangeDetector<T>: ChangeDetector<T> {

    companion object {
        val log by logger()
    }

    private val listeners: MutableList<ChangeListener<T>> = mutableListOf()

    override fun addChangeListener(listener: ChangeListener<T>) {
        this.listeners.add(listener)
    }

    override fun removeChangeListener(listener: ChangeListener<T>) {
        this.listeners.remove(listener)
    }

    private fun doDetectionProcedure() {
        val changedResource = getChangedResource() ?: return

        val changeEvent = ChangeEvent(Instant.now(), changedResource)
        listeners.forEach {
            it.onChangeDetected(changeEvent)
        }
    }

    abstract fun getChangedResource(): T?

    override fun run() {
        doDetectionProcedure()
    }
}