package nsl.sam.changes

interface ChangeListener<T> {
    fun onChangeDetected(changeEvent: ChangeEvent<T>)
}
