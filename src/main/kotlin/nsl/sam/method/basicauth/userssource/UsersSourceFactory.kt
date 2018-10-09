package nsl.sam.method.basicauth.userssource

import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.core.env.Environment

interface UsersSourceFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): UsersSource
}