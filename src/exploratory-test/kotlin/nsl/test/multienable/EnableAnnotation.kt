package nsl.test.multienable

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Import(ConfigurationBean::class)
annotation class EnableAnnotation