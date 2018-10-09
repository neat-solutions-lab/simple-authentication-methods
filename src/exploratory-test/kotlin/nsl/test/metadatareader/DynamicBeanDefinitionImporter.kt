package nsl.test.metadatareader

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import kotlin.reflect.full.cast

class DynamicBeanDefinitionImporter : ImportBeanDefinitionRegistrar, BeanFactoryAware {

    lateinit var listableBeanFactory: ListableBeanFactory

    companion object {
        val cachingMetadataReaderFactory = CachingMetadataReaderFactory()
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.listableBeanFactory = ListableBeanFactory::class.cast(beanFactory)
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

        println(">>>> ${this::class.simpleName}.registerBeansDefinitions(...) called")


        val annotatedConfigurations = this.listableBeanFactory.getBeanNamesForAnnotation(EnableNothing::class.java)

        annotatedConfigurations.forEach {
            val configurationType = this.listableBeanFactory.getType(it)
            val metaDataReader = cachingMetadataReaderFactory.getMetadataReader(configurationType!!.name)
            val annotationMetadata = metaDataReader.annotationMetadata
            println("annotation metadata: $annotationMetadata")

            val annotationAttributes = AnnotationAttributes.fromMap(
                    annotationMetadata.getAnnotationAttributes(
                            EnableNothing::class.qualifiedName!!, false
                    )
            )
            println("annotation attributes: $annotationAttributes")
        }
    }
}