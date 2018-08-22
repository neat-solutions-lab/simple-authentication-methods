package nsl.sam.functional.multiannotation

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = [MultiEnabledAnnotationFunctionalTestConfig::class,
                   MultiEnabledAnnotationFunctionalTestConfigOne::class,
                   MultiEnabledAnnotationFunctionalTestConfigTwo::class])
@AutoConfigureMockMvc
class MultiEnabledAnnotationFunctionalTest {

    @Test
    fun contextLoaded() {

    }

}