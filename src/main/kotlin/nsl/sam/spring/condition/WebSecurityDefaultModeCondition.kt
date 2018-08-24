package nsl.sam.spring.condition

import nsl.sam.spring.annotation.AnnotationProcessor
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.util.Assert

class WebSecurityDefaultModeCondition: Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        Assert.notNull(context.beanFactory, "No bean factory present in ConditionContext.")
        return !AnnotationProcessor.isAtLeastOneAnnotationWithDebugModeEnabled(
                        context.beanFactory!!, context.registry, context.classLoader)
    }
}