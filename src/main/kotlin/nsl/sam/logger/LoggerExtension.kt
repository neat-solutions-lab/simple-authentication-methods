package nsl.sam.logger

import org.slf4j.LoggerFactory
import org.slf4j.Logger

fun <T : Any> T.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this::class.java.name.substringBefore("\$Companion")) }
}
