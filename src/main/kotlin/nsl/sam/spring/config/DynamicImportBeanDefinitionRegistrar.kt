package nsl.sam.spring.config

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

class DynamicImportBeanDefinitionRegistrar: ImportBeanDefinitionRegistrar, BeanFactoryAware {

    lateinit var beanFactoryRef: ListableBeanFactory

    //override fun setBeanFactory(beanFactory: BeanFactory) {
    override fun setBeanFactory(beanFactory: BeanFactory) {

        this.beanFactoryRef = beanFactory as ListableBeanFactory
        //val listableBeanFactory = beanFactory as ListableBeanFactory


        //val configurableListableBeanFactory: ConfigurableListableBeanFactory = beanFactory
        //val beanDefinitionRegistry = beanFactory as BeanDefinitionRegistry


    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        beanFactoryRef.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java)
                .forEach {
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                    println("HHHHHHHHHHHHHH $it")
                }

    }

}