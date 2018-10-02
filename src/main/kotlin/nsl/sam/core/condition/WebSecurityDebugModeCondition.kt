package nsl.sam.core.condition

import nsl.sam.logger.logger
import nsl.sam.annotation.AnnotationAttributeDefinition
import nsl.sam.annotation.AnnotationProcessor
import nsl.sam.annotation.AnnotationProcessorContext
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.util.Assert

class WebSecurityDebugModeCondition: Condition {

    companion object {
        val log by logger()
    }

    @Suppress("unused_parameter")
    private fun matchesInternal(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        Assert.notNull(context.beanFactory, "No bean factory present in ConditionContext.")
        return AnnotationProcessor.isAtLeastOneAnnotationWithSpecifiedAttributeValue(
                AnnotationProcessorContext.fromConditionContext(context),
                AnnotationAttributeDefinition(
                        EnableSimpleAuthenticationMethods::class, "debug", true)
        )
    }

    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val matches = matchesInternal(context, metadata)
        log.info("WebSecurityDebugModeCondition matching result: $matches")
        return matches
    }
}