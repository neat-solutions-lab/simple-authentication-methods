package nsl.sam.spring

import nsl.sam.logger.logger
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.core.type.AnnotationMetadata

@RunWith(MockitoJUnitRunner::class)
class EnabledEntrypointsSelectorTest {

    companion object { val log by logger() }


    //    @Rule // or instead of below @JvmField use only @get:Rule
    //    @JvmField
    //    final val thrown: ExpectedException = ExpectedException.none()


    @Mock
    lateinit var importingClassMetadata: AnnotationMetadata

    @Test
    fun selectImports() {

//        // ARRANGE
//        val subject = EnabledEntrypointsSelector()
//
//        // ACT
//        val result = subject.selectImports(importingClassMetadata)
//
//        // ASSERT
//        log.info("Selected configurations:")
//        result.forEach { log.info("${it}") }

    }
}