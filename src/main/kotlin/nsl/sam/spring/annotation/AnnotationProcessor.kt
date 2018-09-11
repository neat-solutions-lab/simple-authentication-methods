package nsl.sam.spring.annotation

import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import kotlin.reflect.KClass

object AnnotationProcessor {

    fun <A: Annotation> isAtLeastOneAnnotationWithSpecifiedAttributeValue(
            processorContext: AnnotationProcessorContext,
            annotationAttributeDefinition: AnnotationAttributeDefinition<A>): Boolean {

        val namesOfAnnotatedBeans = processorContext.beanFactory
                .getBeanNamesForAnnotation(annotationAttributeDefinition.annotationClass.java)

        for(beanName in namesOfAnnotatedBeans) {
            val beanClassName = processorContext.beanDefinitionRegistry
                    .getBeanDefinition(beanName).beanClassName
            Assert.notNull(beanClassName, "beanClassName in bean definition cannot be null")
            val attrValue =
                    getAnnotationAttributeValueForBeanName(
                            beanClassName!!,
                            annotationAttributeDefinition.annotationClass,
                            annotationAttributeDefinition.attrName,
                            processorContext.classLoader)
            if(attrValue == annotationAttributeDefinition.attrValue) return true
        }
        return false
    }

    private fun <A: Annotation> getAnnotationAttributeValueForBeanName(
            beanName: String, annotationClass: KClass<A>, attrName:String, classLoader: ClassLoader?): Any? {
        val beanClass = ClassUtils.forName(beanName, classLoader)
        Assert.notNull(beanClass, "Class of bean of name $beanName cannot be null.")
        val annotation = AnnotationUtils.findAnnotation(beanClass, annotationClass.java)
        return AnnotationUtils.getValue(annotation, attrName)
    }
}