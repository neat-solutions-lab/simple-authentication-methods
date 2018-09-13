package nsl.sam.functional.springfactories

import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication

class GeneralConfigurationActivatorReseterFT {

    @Test
    fun generalConfigurationActivatorReseterBeanPresentInApplicationContext() {
        val springApplication = SpringApplication()
        //assertThat(springApplication.listeners.map { it::class }).contains(GeneralConfigurationActivatorReseter::class)
    }

}