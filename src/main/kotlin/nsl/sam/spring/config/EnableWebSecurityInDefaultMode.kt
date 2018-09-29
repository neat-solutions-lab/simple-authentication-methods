package nsl.sam.spring.config

import nsl.sam.spring.condition.SimpleNoMethodValueIsAbsent
import nsl.sam.spring.condition.WebSecurityDefaultModeCondition
import org.springframework.context.annotation.Conditional
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Conditional(SimpleNoMethodValueIsAbsent::class, WebSecurityDefaultModeCondition::class)
@EnableWebSecurity
class EnableWebSecurityInDefaultMode