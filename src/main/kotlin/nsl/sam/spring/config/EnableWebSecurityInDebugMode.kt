package nsl.sam.spring.config

import nsl.sam.spring.condition.WebSecurityDebugModeCondition
import org.springframework.context.annotation.Conditional
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Conditional(WebSecurityDebugModeCondition::class)
@EnableWebSecurity(debug = true)
class EnableWebSecurityInDebugMode