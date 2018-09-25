package nsl.sam.spring.config.ordering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OrderingManagerTest {

    @Test
    fun occupyNumber() {
        val orderingManager = OrderingManager()
        (1..100).forEach { orderingManager.occupyNumber(it) }
        val value = orderingManager.getNextNumber()
        assertThat(value).isEqualTo(101)
    }

    @Test
    fun getNextNumber() {
        val orderingManager = OrderingManager()
        for(i in 1..100) {
            orderingManager.getNextNumber()
        }
        val value = orderingManager.getNextNumber()
        assertThat(value).isEqualTo(101)
    }

    @Test
    fun cannotOccupyAlreadyReturnedValue() {
        val orderingManager = OrderingManager()
        orderingManager.getNextNumber()
        assertThrows(IllegalStateException::class.java) {
            orderingManager.occupyNumber(1)
        }
    }

}