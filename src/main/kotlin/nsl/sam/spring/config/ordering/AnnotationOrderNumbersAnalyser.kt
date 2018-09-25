package nsl.sam.spring.config.ordering

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import org.springframework.util.Assert

class AnnotationOrderNumbersAnalyser {


    val metadataReaderFactory = SimpleMetadataReaderFactory()

    private fun analyseBeanType(bean: Class<*>) {

        val metadataReader = metadataReaderFactory.getMetadataReader(bean.canonicalName)
        val annotationMetadata = metadataReader.annotationMetadata

        val annotationAttributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(
                        EnableSimpleAuthenticationMethods::class.qualifiedName!!, false
                )
        )

        val orderValue:Int = annotationAttributes?.get("order") as Int

        println(">>>>>>>>>>> orderValue: $orderValue")

        //if (orderValue == -1) {
        //
        //}

    }

    fun analyse(listableBeanFactory: ListableBeanFactory) {

        val enabledConfigurations = listableBeanFactory.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java)

        enabledConfigurations.forEach {
            val beanType = listableBeanFactory.getType(it)
            Assert.notNull(beanType, "Inconsistency detected. There is bean name in bean factory, but there is no info about type.")
            analyseBeanType(beanType!!)
        }


    }

}