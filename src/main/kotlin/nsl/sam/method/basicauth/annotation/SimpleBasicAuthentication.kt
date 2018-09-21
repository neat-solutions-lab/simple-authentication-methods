package nsl.sam.method.basicauth.annotation

import org.springframework.beans.factory.annotation.Value

@Target(AnnotationTarget.CLASS)
annotation class SimpleBasicAuthentication (

    val passwordsFilePropertyName: String = "",

    val passwordsFilePath: String = ""
)