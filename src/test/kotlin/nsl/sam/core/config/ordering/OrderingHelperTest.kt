package nsl.sam.core.config.ordering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class OrderingHelperTest {

    @Test
    fun occupyNumber() {
        val orderingManager = OrderingHelper()
        (1..100).forEach { orderingManager.occupyNumber(it) }
        val value = orderingManager.getNextNumber()
        assertThat(value).isEqualTo(101)
    }

    @Test
    fun getNextNumber() {
        val orderingManager = OrderingHelper()
        for (i in 1..100) {
            orderingManager.getNextNumber()
        }
        val value = orderingManager.getNextNumber()
        assertThat(value).isEqualTo(101)
    }

    @Test
    fun cannotOccupyAlreadyReturnedValue() {
        val orderingManager = OrderingHelper()
        orderingManager.getNextNumber()
        assertThrows(IllegalStateException::class.java) {
            orderingManager.occupyNumber(1)
        }
    }
}