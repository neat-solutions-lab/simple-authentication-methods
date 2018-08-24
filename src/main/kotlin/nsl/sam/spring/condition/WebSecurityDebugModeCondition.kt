package nsl.sam.spring.condition

import nsl.sam.spring.annotation.AnnotationAttributeDefinition
import nsl.sam.spring.annotation.AnnotationProcessor
import nsl.sam.spring.annotation.AnnotationProcessorContext
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.util.Assert

class WebSecurityDebugModeCondition: Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        Assert.notNull(context.beanFactory, "No bean factory present in ConditionContext.")
        return AnnotationProcessor.isAtLeastOneAnnotationWithSpecifiedAttributeValue(
                AnnotationProcessorContext.fromConditionContext(context),
                AnnotationAttributeDefinition(EnableSimpleAuthenticationMethods::class, "debug", true)
        )
    }
}