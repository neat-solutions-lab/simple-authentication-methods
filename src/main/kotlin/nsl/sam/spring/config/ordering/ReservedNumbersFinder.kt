package nsl.sam.spring.config.ordering

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import org.springframework.util.Assert

class ReservedNumbersFinder(val listableBeanFactory: ListableBeanFactory) {

    val metadataReaderFactory = SimpleMetadataReaderFactory()


    fun findReservedNumbers(): List<Int> {
        return processBeanFactory()
    }

    private fun processBeanFactory(): List<Int> {
        val configuratorBeansNames = listableBeanFactory.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java)
        return processBeansNames(configuratorBeansNames)
    }

    private fun processBeansNames(beansNames: Array<String>): List<Int> {

        val reservedValues = mutableListOf<Int>()

        beansNames.forEach {
            val beanType = listableBeanFactory.getType(it)
            Assert.notNull(beanType, "The bean name $it is registered but there is no information about type of this beean")
            val orderValue = processBeanType(beanType!!)
            if(orderValue!=-1) {
                reservedValues.add(orderValue)
            }
        }

        return reservedValues
    }

    private fun processBeanType(beanType: Class<*>): Int {

        val metadataReader = metadataReaderFactory.getMetadataReader(beanType.canonicalName)
        val annotationMetadata = metadataReader.annotationMetadata

        val annotationAttributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        EnableSimpleAuthenticationMethods::class.qualifiedName!!, false
                )
        )
        val orderValue = annotationAttributes?.get("order") as Int?
        return orderValue ?: -1
    }
}