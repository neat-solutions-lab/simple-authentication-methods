package nsl.sam.concurrent

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.lang.Exception
import java.util.concurrent.locks.ReentrantReadWriteLock

internal class ConditionalReadWriteSynchronizerTest {

    @Test
    fun oneReadLockWhenInsideReadLockBlock() {

        var beforeBlockReadLockCount: Int
        var inBlockReadLockCount = 0
        var afterBlockReadLockCount: Int

        val conditionalSynchronizer = ConditionalReadWriteSynchronizer(true)

        val innerReadWriteLock =
                ReflectionTestUtils.getField(conditionalSynchronizer, "readWriteLock") as ReentrantReadWriteLock

        beforeBlockReadLockCount = innerReadWriteLock.readLockCount

        conditionalSynchronizer.readLock {
            inBlockReadLockCount = innerReadWriteLock.readLockCount
        }

        afterBlockReadLockCount = innerReadWriteLock.readLockCount

        Assertions.assertThat(beforeBlockReadLockCount).isEqualTo(0)
        Assertions.assertThat(inBlockReadLockCount).isEqualTo(1)
        Assertions.assertThat(afterBlockReadLockCount).isEqualTo(0)
    }

    @Test
    fun readLockReleasedWhenExceptionThrown() {

        var inBlockReadLockCount = 0
        var afterBlockReadLockCount: Int

        val conditionalSynchronizer = ConditionalReadWriteSynchronizer(true)

        val innerReadWriteLock =
                ReflectionTestUtils.getField(conditionalSynchronizer, "readWriteLock") as ReentrantReadWriteLock

        try {
            conditionalSynchronizer.readLock {
                inBlockReadLockCount = innerReadWriteLock.readLockCount
                throw Exception("Contrived exception")
            }
        } catch (e: Exception) {
            afterBlockReadLockCount = innerReadWriteLock.readLockCount
            println("exception: ${e.message}")
        }

        Assertions.assertThat(inBlockReadLockCount).isEqualTo(1)
        Assertions.assertThat(afterBlockReadLockCount).isEqualTo(0)
    }

    @Test
    fun oneWriteLockHoldWhenInsideWriteLockBlock() {
        var beforeBlockWriteLockCount: Int
        var inBlockWriteLockCount = 0
        var afterBlockWriteLockCount: Int

        val conditionalSynchronizer = ConditionalReadWriteSynchronizer(true)

        val innerReadWriteLock =
                ReflectionTestUtils.getField(conditionalSynchronizer, "readWriteLock") as ReentrantReadWriteLock

        beforeBlockWriteLockCount = innerReadWriteLock.writeHoldCount

        conditionalSynchronizer.writeLock {
            inBlockWriteLockCount = innerReadWriteLock.writeHoldCount
        }

        afterBlockWriteLockCount = innerReadWriteLock.writeHoldCount

        Assertions.assertThat(beforeBlockWriteLockCount).isEqualTo(0)
        Assertions.assertThat(inBlockWriteLockCount).isEqualTo(1)
        Assertions.assertThat(afterBlockWriteLockCount).isEqualTo(0)
    }

    @Test
    fun writeLockReleasedWhenExceptionThrown() {

        var inBlockWriteLockCount = 0
        var afterBlockWriteLockCount = 0

        val conditionalSynchronizer = ConditionalReadWriteSynchronizer(true)

        val innerReadWriteLock =
                ReflectionTestUtils.getField(conditionalSynchronizer, "readWriteLock") as ReentrantReadWriteLock

        try {
            conditionalSynchronizer.writeLock {
                inBlockWriteLockCount = innerReadWriteLock.writeHoldCount
                throw Exception("Contrived exception")
            }
        } catch (e: Exception) {
            afterBlockWriteLockCount = innerReadWriteLock.writeHoldCount
            println("exception: ${e.message}")
        }

        Assertions.assertThat(inBlockWriteLockCount).isEqualTo(1)
        Assertions.assertThat(afterBlockWriteLockCount).isEqualTo(0)
    }
}