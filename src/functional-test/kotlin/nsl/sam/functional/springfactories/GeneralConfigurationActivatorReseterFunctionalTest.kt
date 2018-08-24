package nsl.sam.functional.springfactories

import nsl.sam.spring.config.GeneralConfigurationActivatorReseter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.boot.SpringApplication

class GeneralConfigurationActivatorReseterFunctionalTest {

    @Test
    fun generalConfigurationActivatorReseterBeanPresentInApplicationContext() {
        val springApplication = SpringApplication()
        assertThat(springApplication.listeners.map { it::class }).contains(GeneralConfigurationActivatorReseter::class)
    }

}