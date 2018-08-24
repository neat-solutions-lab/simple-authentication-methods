package nsl.sam.spring.annotation

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import kotlin.test.assertNotNull

class AnnotationProcessor {

    companion object {
        fun isAtLeastOneAnnotationWithDebugModeEnabled(
                beanFactory: ConfigurableListableBeanFactory,
                beanDefinitionRegistry: BeanDefinitionRegistry,
                classLoader: ClassLoader?): Boolean {

            val namesOfAnnotatedBeans =
                    beanFactory.getBeanNamesForAnnotation(EnableSimpleAuthenticationMethods::class.java)

            for(beanName in namesOfAnnotatedBeans) {
                val beanClassName = beanDefinitionRegistry.getBeanDefinition(beanName).beanClassName
                Assert.notNull(beanClassName, "beanClassName in bean definition cannot be null")
                val attrValue:Boolean =
                        getAnnotationAttributeValueForBeanName(beanClassName!!,EnableSimpleAuthenticationMethods::class.java,"debug", classLoader) as Boolean
                if(attrValue) return true
//                val beanClass = ClassUtils.forName(beanClassName!!, classLoader)
//                Assert.notNull(beanClass, "Class of bean of name $beanClassName cannot be null.")
//                val annotation = AnnotationUtils.findAnnotation(beanClass!!, EnableSimpleAuthenticationMethods::class.java)
//                val debugValue = AnnotationUtils.getValue(annotation, "debug")
//                if (debugValue == true) return true
            }
            return false
        } // fun()

        private fun <A: Annotation> getAnnotationAttributeValueForBeanName(
                beanName: String, annotationClass: Class<A>, attrName:String, classLoader: ClassLoader?): Any? {
            val beanClass = ClassUtils.forName(beanName, classLoader)
            Assert.notNull(beanClass, "Class of bean of name $beanName cannot be null.")
            val annotation = AnnotationUtils.findAnnotation(beanClass!!, annotationClass)
            return AnnotationUtils.getValue(annotation, attrName)
        }
    }
}