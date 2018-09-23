package nsl.sam.configurer

import nsl.sam.spring.annotation.AuthenticationMethod
import nsl.sam.spring.annotation.EnableAnnotationAttributes

interface AuthMethodInternalConfigurerFactory {

    val name: String
    fun getSupportedMethod(): AuthenticationMethod
    fun create(attributes: EnableAnnotationAttributes): AuthMethodInternalConfigurer

}