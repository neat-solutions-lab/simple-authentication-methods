package nsl.sam.spring.annotation

import nsl.sam.logger.logger
import nsl.sam.spring.config.*
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import java.time.Instant

class EnabledEntrypointsSelector: ImportSelector {

    companion object {
        val log by logger()
    }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        DynamicBeansRegistar.enableAnnotations.add(importingClassMetadata)


        val configurationClasses : ArrayList<String> = ArrayList(5)

        log.info("${SimpleWebSecurityConfigurer::class.qualifiedName} added to configuration classes [${Instant.now().nano}]")
        configurationClasses.add(SimpleWebSecurityConfigurer::class.qualifiedName!!)

        val attributes: AnnotationAttributes? =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(
                                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
                )

        val enabledAuthMethods: Array<AuthenticationMethod> = attributes?.let {
            attributes[("methods")] as Array<AuthenticationMethod>
        } ?: emptyArray()

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

        val basicAuthConfigClass = configurationClasses.find { it == BasicAuthConfig::class.qualifiedName }
        if(null == basicAuthConfigClass) {
            log.info("${DisableBasicAuthConfig::class.qualifiedName} added to configuration classes")
            configurationClasses.add(DisableBasicAuthConfig::class.qualifiedName!!)
        }

        return configurationClasses.toTypedArray()
    }
}