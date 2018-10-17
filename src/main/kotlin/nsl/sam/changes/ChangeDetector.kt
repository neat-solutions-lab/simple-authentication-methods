package nsl.sam.changes

interface ChangeDetector<T>: Runnable {
    fun addChangeListener(listener: ChangeListener<T>)
    fun removeChangeListener(listener: ChangeListener<T>)
}