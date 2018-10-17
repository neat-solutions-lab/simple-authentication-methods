package nsl.sam.changes

interface Changable<T> {
    fun getChangeDetector(): ChangeDetector<T>
}