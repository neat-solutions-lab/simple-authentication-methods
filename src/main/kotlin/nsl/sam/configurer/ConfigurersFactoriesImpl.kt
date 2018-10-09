package nsl.sam.configurer

import nsl.sam.core.annotation.AuthenticationMethod

class ConfigurersFactoriesImpl : ConfigurersFactories {

    val factoriesMap: MutableMap<AuthenticationMethod, MutableList<AuthMethodInternalConfigurerFactory>> = mutableMapOf()

    override fun getFactoryForMethod(authenticationMethod: AuthenticationMethod): AuthMethodInternalConfigurerFactory? {
        return factoriesMap[authenticationMethod]?.get(0)
    }

    override fun getAllFactories(): Collection<AuthMethodInternalConfigurerFactory> {
        return factoriesMap.values.flatten()
    }

    override fun addFactory(factory: AuthMethodInternalConfigurerFactory) {

        val authenticationMethod = factory.getSupportedMethod()
        val listOfFactories = factoriesMap.getOrPut(authenticationMethod) { mutableListOf() }
        listOfFactories.add(factory)

    }
}