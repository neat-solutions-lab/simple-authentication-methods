package nsl.sam.concurrent

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class ConditionalReadWriteSynchronizer(private val condition: Boolean) {

    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock(true)

    fun <T> readLock(block: ()->T): T {
        try {
            if(condition) readWriteLock.readLock().lock()
            return block()
        } finally {
            if(condition) readWriteLock.readLock().unlock()
        }
    }

    fun writeLock(block: ()->Unit) {

        try {
            if(condition) readWriteLock.writeLock().lock()
            block()
        } finally {
            if(condition) readWriteLock.writeLock().unlock()
        }
    }
}