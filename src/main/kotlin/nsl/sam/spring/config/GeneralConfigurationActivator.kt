package nsl.sam.spring.config

import nsl.sam.logger.logger
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class GeneralConfigurationActivator: ImportSelector {

    companion object {
        val log by logger()
        var alreadyCalled = false
    }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        log.info("selectImports() in ${this::class.simpleName} called.")

        if(alreadyCalled) {
            log.info("Skipping adding ${GeneralConfiguration::class.simpleName}, it already has been added.")
            return emptyArray()
        } else {
            alreadyCalled = true
        }

        log.info("Adding configuration bean: ${GeneralConfiguration::class.simpleName}")
        return arrayOf(
                GeneralConfiguration::class.qualifiedName!!
        )
    }
}