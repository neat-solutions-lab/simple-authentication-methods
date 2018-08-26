package nsl.sam.functional.disabled;

import nsl.sam.spring.annotation.EnableSimpleAuthenticationMethods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = {PureJavaTest.TestConfiguration.class})
@AutoConfigureMockMvc
@ContextConfiguration
public class PureJavaTest {

    @Test
    public void exampleTest() {

    }

    @Configuration
    @EnableSimpleAuthenticationMethods(methods = {})
    static class TestConfiguration{}
}
