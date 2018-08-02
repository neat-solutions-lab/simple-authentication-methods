package nsl.sam.spring.annotation

import nsl.sam.spring.config.BasicAuthConfig
import nsl.sam.spring.config.TokenAuthConfig
import nsl.sam.spring.config.WebSecurityConfigurer
import nsl.sam.logger.logger
import nsl.sam.spring.config.DisableBasicAuthConfig
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import java.time.Instant

class SimpleAuthenticationMethodsSelector: ImportSelector {

    companion object { val log by logger() }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        val configurationClasses : ArrayList<String> = ArrayList(5)

        log.info("${WebSecurityConfigurer::class.qualifiedName} added to configuration classes [${Instant.now().nano}]")
        configurationClasses.add(WebSecurityConfigurer::class.qualifiedName!!)

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
                    log.info("${TokenAuthConfig::class.qualifiedName} added to configuration classes.")
                    configurationClasses.add(TokenAuthConfig::class.qualifiedName!!)
                }
                AuthenticationMethod.SIMPLE_BASIC_AUTH -> {
                    log.info("${BasicAuthConfig::class.qualifiedName} added to configuration classes.")
                    configurationClasses.add(BasicAuthConfig::class.qualifiedName!!)
                }
            }
        }

        val foundClass = configurationClasses.find { it::class == BasicAuthConfig::class }
        if(null == foundClass) {
            log.info("${DisableBasicAuthConfig::class.qualifiedName} added to configuration classes")
            configurationClasses.add(DisableBasicAuthConfig::class.qualifiedName!!)
        }

        return configurationClasses.toTypedArray()
    }
}