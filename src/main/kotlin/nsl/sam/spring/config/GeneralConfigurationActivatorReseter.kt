package nsl.sam.spring.config

import nsl.sam.logger.logger
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

class GeneralConfigurationActivatorReseter: ApplicationListener<ContextRefreshedEvent> {
    companion object {
        val log by logger()
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("Reseting ${GeneralConfigurationActivator::class.simpleName} state on ContextRefreshedEvent event")
        GeneralConfigurationActivator.alreadyCalled = false
    }

}