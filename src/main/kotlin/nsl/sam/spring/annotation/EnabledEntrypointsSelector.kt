package nsl.sam.spring.annotation

import nsl.sam.logger.logger
import nsl.sam.spring.config.*
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class EnabledEntrypointsSelector: ImportSelector {

    companion object { val log by logger() }

    private fun matchesSimpleNoMethod(method: AuthenticationMethod): Boolean {
        return method == AuthenticationMethod.SIMPLE_NO_METHOD
    }

    private fun getEnabledMethods(importingClassMetadata: AnnotationMetadata):  Array<AuthenticationMethod> {

        val annotationAttributes: AnnotationAttributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(
                                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
                ) ?: return arrayOf()

        val enabledMethods: Array<AuthenticationMethod> = annotationAttributes.run {
            get("methods") as Array<AuthenticationMethod>
        }

        enabledMethods
                .firstOrNull { matchesSimpleNoMethod(it) }
                ?.let { return arrayOf() }

        return annotationAttributes["methods"] as Array<AuthenticationMethod>
    }

    private fun getMethodConfigurationClass(method: AuthenticationMethod): KClass<*> =  when(method) {
            AuthenticationMethod.SIMPLE_TOKEN -> TokenAuthConfig::class
            AuthenticationMethod.SIMPLE_BASIC_AUTH -> BasicAuthConfig::class
            else -> throw IllegalArgumentException("Illegal AuthenticationMethod used: $method")
    }

    private fun addMethodToBuilder(method: AuthenticationMethod, builder: ConfigurationClassesBuilder) {
        builder.add(getMethodConfigurationClass(method))
    }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        log.debug("selectImports() in ${this::class.simpleName} called.")

        DynamicBeansRegistar.enableAnnotations.add(importingClassMetadata)

        val configurationClassesBuilder = ConfigurationClassesBuilder()

        configurationClassesBuilder.add(SimpleWebSecurityConfigurer::class)
        log.info("${SimpleWebSecurityConfigurer::class.qualifiedName} added to configuration classes")

        val enabledAuthMethods = getEnabledMethods(importingClassMetadata)
        log.debug("Found enabled methods: $enabledAuthMethods")

        enabledAuthMethods.forEach {
            addMethodToBuilder(it, configurationClassesBuilder)
            log.info("${getMethodConfigurationClass(it)} added to configuration classes.")
        }

        configurationClassesBuilder.addIfNoConflict(
                DisableBasicAuthConfig::class,
                BasicAuthConfig::class,
                {log.info("${DisableBasicAuthConfig::class.qualifiedName} added to configuration classes")}
        )

        return configurationClassesBuilder.build()
    }
}