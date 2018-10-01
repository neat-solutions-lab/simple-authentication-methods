package nsl.sam.spring.condition

import nsl.sam.logger.logger
import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class SimpleNoMethodValueIsAbsent: Condition {

    companion object {
        val log by logger()
    }

    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {

        val methodsAttributeValue = metadata.getAnnotationAttributes(
                EnableSimpleAuthenticationMethods::class.qualifiedName!!)?.get("methods")
                as Array<AuthenticationMethod>?

        methodsAttributeValue?.forEach {
            if(it == AuthenticationMethod.SIMPLE_NO_METHOD) {
            log.info("Skipping processing ${EnableSimpleAuthenticationMethods::class.simpleName} annotation " +
                    "because one of declared methods is ${AuthenticationMethod.SIMPLE_NO_METHOD.name}")
                return false
            }
        }

        return true
    }
}
