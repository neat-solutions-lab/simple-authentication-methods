package nsl.sam.spring.config

import nsl.sam.logger.logger
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.ImportSelector
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.type.AnnotationMetadata

class GeneralConfigurationActivator: ImportSelector/*, ApplicationListener<ContextRefreshedEvent>*/ {

    companion object {
        val log by logger()
        var alreadyCalled = false
    }

/*
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("Reseting ${this::class.simpleName} state on ContextRefreshedEvent")
        alreadyCalled = false
    }
*/

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        log.info("selectImports() in ${this::class.simpleName} called.")

        if(alreadyCalled) {
            log.info("Skipping adding ${SimpleAuthenticationMethodsGeneralConfiguration::class.simpleName}, it already has been added.")
            return emptyArray()
        }
        alreadyCalled = true

        log.info("Adding configuration bean: ${SimpleAuthenticationMethodsGeneralConfiguration::class.simpleName}")
        return arrayOf(
                SimpleAuthenticationMethodsGeneralConfiguration::class.qualifiedName!!
        )
    }
}