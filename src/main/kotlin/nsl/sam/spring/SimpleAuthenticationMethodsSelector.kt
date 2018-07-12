package nsl.sam.spring

import nsl.sam.configurator.BasicAuthConfigurator
import nsl.sam.configurator.TokenAuthConfigurator
import nsl.sam.configurator.WebSecurityConfigurator
import nsl.sam.logger.logger
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata

class SimpleAuthenticationMethodsSelector: ImportSelector {

    companion object { val log by logger() }

    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {

        val configurationClasses : ArrayList<String> = ArrayList(5)

        configurationClasses.add(WebSecurityConfigurator::class.qualifiedName!!)

        val attributes: AnnotationAttributes? =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(
                                EnableSimpleAuthenticationMethods::class.qualifiedName!!, false)
                )

        attributes ?: return configurationClasses.toTypedArray()

        val methods: Array<AuthenticationMethod> = attributes[("methods")] as Array<AuthenticationMethod>

        methods.forEach {
            when(it) {
                AuthenticationMethod.SIMPLE_TOKEN ->
                    configurationClasses.add(TokenAuthConfigurator::class.qualifiedName!!)
                AuthenticationMethod.SIMPLE_BASIC_AUTH ->
                    configurationClasses.add(BasicAuthConfigurator::class.qualifiedName!!)
            }
        }
        return configurationClasses.toTypedArray()
    }
}