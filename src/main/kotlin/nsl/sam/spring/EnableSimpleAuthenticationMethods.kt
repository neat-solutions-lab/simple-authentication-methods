package nsl.sam.spring

import nsl.sam.configurator.WebSecurityConfigurator
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Import(SimpleAuthenticationMethodsSelector::class)
annotation class EnableSimpleAuthenticationMethods(
        val methods: Array<AuthenticationMethod> =
                [AuthenticationMethod.SIMPLE_BASIC_AUTH, AuthenticationMethod.SIMPLE_TOKEN]
)
