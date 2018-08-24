package nsl.sam.spring.annotation

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ConditionContext

data class AnnotationProcessorContext(
        val beanFactory: ConfigurableListableBeanFactory,
        val beanDefinitionRegistry: BeanDefinitionRegistry,
        val classLoader: ClassLoader?) {

    companion object {
        fun fromConditionContext(conditionContext: ConditionContext): AnnotationProcessorContext {
            return AnnotationProcessorContext(
                    conditionContext.beanFactory!!,
                    conditionContext.registry,
                    conditionContext.classLoader)
        }
    }
}