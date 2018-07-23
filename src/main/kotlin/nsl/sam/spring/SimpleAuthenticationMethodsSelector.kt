package nsl.sam.spring

import nsl.sam.configurator.BasicAuthConfigurator
import nsl.sam.configurator.TokenAuthConfigurator
import nsl.sam.configurator.WebSecurityConfigurator
import nsl.sam.logger.logger
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import java.time.Instant

class SimpleAuthenticationMethodsSelector: ImportSelector {

    companion object { val log by logger() }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        val configurationClasses : ArrayList<String> = ArrayList(5)

        log.info("${WebSecurityConfigurator::class.qualifiedName} added to configuration classes [${Instant.now().nano}]")
        configurationClasses.add(WebSecurityConfigurator::class.qualifiedName!!)

        val attributes: AnnotationAttributes? =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(
                                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
                )

        attributes ?: return configurationClasses.toTypedArray()

        val enabledAuthMethods: Array<AuthenticationMethod> = attributes[("methods")] as Array<AuthenticationMethod>

        enabledAuthMethods.forEach {
            when(it) {
                AuthenticationMethod.SIMPLE_TOKEN -> {
                    log.info("${TokenAuthConfigurator::class.qualifiedName} added to configuration classes.")
                    configurationClasses.add(TokenAuthConfigurator::class.qualifiedName!!)
                }
                AuthenticationMethod.SIMPLE_BASIC_AUTH -> {
                    log.info("${BasicAuthConfigurator::class.qualifiedName} added to configuration classes.")
                    configurationClasses.add(BasicAuthConfigurator::class.qualifiedName!!)
                }
            }
        }
        return configurationClasses.toTypedArray()
    }
}