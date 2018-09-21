package nsl.sam.configurer

import nsl.sam.spring.annotation.AuthenticationMethod

interface AuthMethodInternalConfigurerFactory {

    val name: String
    fun getSupportedMethod(): AuthenticationMethod
    fun create(attributes: Any): AuthMethodInternalConfigurer

}