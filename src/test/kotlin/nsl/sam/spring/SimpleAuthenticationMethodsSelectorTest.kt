package nsl.sam.spring

import nsl.sam.logger.logger
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.core.type.AnnotationMetadata

@RunWith(MockitoJUnitRunner::class)
class SimpleAuthenticationMethodsSelectorTest {

    companion object { val log by logger() }

    @Mock
    lateinit var importingClassMetadata: AnnotationMetadata

    @Test
    fun selectImports() {

//        // ARRANGE
//        val subject = SimpleAuthenticationMethodsSelector()
//
//        // ACT
//        val result = subject.selectImports(importingClassMetadata)
//
//        // ASSERT
//        log.info("Selected configurations:")
//        result.forEach { log.info("${it}") }

    }
}