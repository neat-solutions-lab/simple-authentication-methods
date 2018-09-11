package nsl.test.dynamicbean.annotation

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Import(DynamicBeansRegistar::class)
annotation class EnableSomething