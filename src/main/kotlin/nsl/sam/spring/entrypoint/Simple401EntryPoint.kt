package nsl.sam.spring.entrypoint

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint

class Simple401EntryPoint: BasicAuthenticationEntryPoint() {
    init {
        realmName = "simple-authentication-methods"
    }
}