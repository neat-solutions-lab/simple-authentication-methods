package nsl.sam.core.config.ordering

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.logger.logger
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.util.Assert
import java.util.*

class ReservedNumbersFinder(val listableBeanFactory: ListableBeanFactory) {

    companion object {
        val log by logger()
    }

    private val metadataReaderFactory = SimpleMetadataReaderFactory()


    fun findReservedNumbers(): List<Int> {
        return processBeanFactory()
    }

    private fun processBeanFactory(): List<Int> {

        //listableBeanFactory.getBeansOfType(WebSecurityConfigurerAdapter::class.java)

        val configuratorBeansNames = listableBeanFactory.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java)
        log.debug("Beans annotated with EnableSimpleAuthenticationMethods: ${Arrays.toString(configuratorBeansNames)}")
        return processBeansNames(configuratorBeansNames)
    }

    private fun processBeansNames(beansNames: Array<String>): List<Int> {

        val reservedValues = mutableListOf<Int>()

        beansNames.forEach {
            val beanType = listableBeanFactory.getType(it)
            Assert.notNull(beanType, "The bean name $it is registered but there is no information about type of this beean")
            val orderValue = processBeanType(beanType!!)
            log.debug("Oreder number for bean $it is $orderValue")
            if(orderValue!=-1) {
                log.debug("Adding the $orderValue order value to the reserved values")
                reservedValues.add(orderValue)
            }
        }

        log.debug("List of reserved order values: $reservedValues")

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
        log.debug("${EnableSimpleAuthenticationMethods::class.qualifiedName!!} annotationAttribures for $beanType: ${annotationAttributes?.keys}")
        val orderValue = annotationAttributes?.get("order") as Int?
        return orderValue ?: -1
    }
}