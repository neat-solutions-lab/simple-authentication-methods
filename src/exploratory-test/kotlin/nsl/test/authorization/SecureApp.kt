package nsl.test.authorization

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.*
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
fun main(args: Array<String>) {
    SpringApplication.run(SecureApp::class.java, *args)
}


@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecureApp

@Suppress("DEPRECATION")
@Configuration
@Order(90)
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN")

    }

    override fun configure(http: HttpSecurity) {
        http.httpBasic()
        http.authorizeRequests().anyRequest().permitAll()
    }

}

@Service
class ProtectedService {

    @PreAuthorize("hasRole('USER')")
    fun index() = "Hello from ProtectedService index()"

    @PreAuthorize("hasRole('ADMIN')")
    fun admin() = "Hello from ProtectedService admin()"

    @PreAuthorize("hasPermission('public','any')")
    fun permissionEvaluatorProtectedPublic() = "Hello from PermissionEvaluator protected service (public) !!!"

    @PreAuthorize("hasPermission('private','any')")
    fun permissionEvaluatorProtectedPrivate() = "Hello from PermissionEvaluator protected service (private) !!!"

    @PreAuthorize("@accessGuard.isAllowed(true)")
    fun accessGuardPublic() = "Hello from AccessGuard protected service (public) !!!"

    @PreAuthorize("@accessGuard.isAllowed(false)")
    fun accessGuardPrivate() = "Hello from AccessGuard protected service (private) !!!"

}

@RestController
class ProtectedController {

    @Autowired
    lateinit var protectedService: ProtectedService

    @GetMapping("/")
    fun index() = protectedService.index()

    @GetMapping("/admin")
    fun admin() = protectedService.admin()

    @GetMapping("/permission-evaluator-public")
    fun permissionEvaluatorPublic() = protectedService.permissionEvaluatorProtectedPublic()

    @GetMapping("/permission-evaluator-private")
    fun permissionEvaluatorPrivate() = protectedService.permissionEvaluatorProtectedPrivate()

    @GetMapping("/access-guard-public")
    fun accessGuardPublic() = protectedService.accessGuardPublic()

    @GetMapping("/access-guard-private")
    fun accessGuardPrivate() = protectedService.accessGuardPrivate()


}

@Component
class CustomPermissionEvaluator:PermissionEvaluator {

    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if(targetDomainObject == "public")
            return true
        return false
    }

    override fun hasPermission(authentication: Authentication?, targetId: Serializable?, targetType: String?, permission: Any?): Boolean {
        return true
    }

}

@Component("accessGuard")
class AccessGuard {

    fun isAllowed(allow: Boolean) = allow

}