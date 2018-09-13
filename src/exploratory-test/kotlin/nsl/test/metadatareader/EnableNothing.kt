package nsl.test.metadatareader

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Import(DynamicBeanDefinitionImporter::class)
annotation class EnableNothing(
        val stringsArrayArray: Array<String> = [],
        val stringAttribute: String = "",
        val integerAttribute: Int = 0,
        val booleanAttribute: Boolean = false
)