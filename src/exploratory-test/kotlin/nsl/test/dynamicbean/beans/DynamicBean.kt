package nsl.test.dynamicbean.beans

import org.springframework.context.annotation.Configuration

@Configuration
class DynamicBean {
    var configurationString: String? = null
}
//data class DynamicBean(val configurationString: String)