package nsl.sam.changes

import java.time.Instant

abstract class AbstractChangeDetector<T>: ChangeDetector<T> {

    private val listeners: MutableList<ChangeListener<T>> = mutableListOf()

    override fun addChangeListener(listener: ChangeListener<T>) {
        this.listeners.add(listener)
    }

    override fun removeChangeListener(listener: ChangeListener<T>) {
        this.listeners.remove(listener)
    }

    private fun changeDetectionProcedure() {
        val changedResource = getChangedResource() ?: return

        val changeEvent = ChangeEvent(Instant.now(), changedResource)
        listeners.forEach {
            it.onChangeDetected(changeEvent)
        }
    }

    abstract fun getChangedResource(): T?

    override fun run() {
        changeDetectionProcedure()
    }
}