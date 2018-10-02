package nsl.sam.configurer

import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.core.annotation.EnableAnnotationAttributes

interface AuthMethodInternalConfigurerFactory {

    val name: String
    fun getSupportedMethod(): AuthenticationMethod
    fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer

}