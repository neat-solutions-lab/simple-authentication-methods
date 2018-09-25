package nsl.sam.spring.config.ordering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ConfigurationOrderingManagerTest {

    @Test
    fun occupyNumber() {
        val value = ConfigurationOrderingManager.getNextNumber()
        assertThrows(IllegalStateException::class.java) {
            ConfigurationOrderingManager.occupyNumber(value)
        }
    }

    @Test
    fun getNextNumber() {
        val firstValue = ConfigurationOrderingManager.getNextNumber()
        val secondValue = ConfigurationOrderingManager.getNextNumber()
        assertThat(secondValue).isGreaterThan(firstValue)
    }
}