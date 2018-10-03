package nsl.sam.core.entrypoint.factory

import nsl.sam.annotation.AnnotationMetadataResolver
import nsl.sam.logger.logger
import org.springframework.core.env.Environment
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.util.Assert
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface AuthenticationEntryPointFactory {

    fun create(): AuthenticationEntryPoint
}