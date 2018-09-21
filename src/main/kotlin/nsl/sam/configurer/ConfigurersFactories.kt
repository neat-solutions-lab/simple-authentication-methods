package nsl.sam.configurer

import nsl.sam.spring.annotation.AuthenticationMethod

interface ConfigurersFactories {

    fun addFactory(factory: AuthMethodInternalConfigurerFactory)
    fun getFactoryForMethod(authenticationMethod: AuthenticationMethod): AuthMethodInternalConfigurerFactory?
    fun getAllFactories():Collection<AuthMethodInternalConfigurerFactory>

}