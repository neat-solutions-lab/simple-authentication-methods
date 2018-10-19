package nsl.sam.changes

interface Changeable<T> {
    fun getChangeDetector(): ChangeDetector<T>?
    fun setChangeDetector(detector: ChangeDetector<T>)
    fun hasChangeDetector(): Boolean
}