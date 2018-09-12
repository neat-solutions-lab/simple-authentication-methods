package nsl.test.dynamicbean.beans

import org.springframework.context.annotation.Configuration

@Configuration
class DynamicBean {
    lateinit var configurationString: String
}
//data class DynamicBean(val configurationString: String)