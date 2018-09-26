package nsl.sam.spring.config.ordering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ConfigurationsOrderingManagerTest {

    @Test
    fun occupyNumber() {
        val value = ConfigurationsOrderingManager.getNextNumber()
        assertThrows(IllegalStateException::class.java) {
            ConfigurationsOrderingManager.occupyNumber(value)
        }
    }

    @Test
    fun getNextNumber() {
        val firstValue = ConfigurationsOrderingManager.getNextNumber()
        val secondValue = ConfigurationsOrderingManager.getNextNumber()
        assertThat(secondValue).isGreaterThan(firstValue)
    }
}