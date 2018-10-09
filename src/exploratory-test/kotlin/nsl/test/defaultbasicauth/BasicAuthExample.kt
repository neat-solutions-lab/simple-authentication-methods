package nsl.test.defaultbasicauth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@SpringBootApplication
@EnableWebSecurity(debug = true)
class BasicAuthExample

fun main(args: Array<String>) {

    SpringApplication.run(BasicAuthExample::class.java, *args)
}

@Configuration
class SecurityConfigurer : WebSecurityConfigurerAdapter() {

    @Bean
    @Suppress("DEPRECATION")
    fun users(): UserDetailsService {
        val users = User.withDefaultPasswordEncoder()
        val manager = InMemoryUserDetailsManager()
        manager.createUser(users.username("user").password("user").roles("USER").build())
        manager.createUser(users.username("admin").password("admin").roles("ADMIN").build())
        return manager
    }

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/protected/**")
                .authorizeRequests()
                .antMatchers("/protected/authenticated/**").authenticated()
                .antMatchers("/protected/anonymous/**").anonymous()
                .antMatchers("/protected/permit-all/**").permitAll()
                .antMatchers("/protected/role-user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/protected/role-admin/**").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .httpBasic()
    }
}

@RestController
@RequestMapping("/protected")
class ProtectedController {

    @GetMapping("/authenticated")
    fun authenticated(request: HttpServletRequest, principal: Principal?, authentication: Authentication?) = responseBody(request, principal, authentication)

    @GetMapping("/anonymous")
    fun anonymous(request: HttpServletRequest, principal: Principal?, authentication: Authentication?) = responseBody(request, principal, authentication)

    @GetMapping("/permit-all")
    fun permitAll(request: HttpServletRequest, principal: Principal?) = responseBody(request, principal)

    @GetMapping("/role-user")
    fun roleUser(request: HttpServletRequest, principal: Principal?) = responseBody(request, principal)

    @GetMapping("/role-admin")
    fun roleAdmin(request: HttpServletRequest, principal: Principal?) = responseBody(request, principal)

    @GetMapping("/deny-all")
    fun denyAll(request: HttpServletRequest, principal: Principal?) = responseBody(request, principal)

    fun responseBody(request: HttpServletRequest, principal: Principal?, authentication: Authentication? = null): String {
        return "EXAMPLE CONTROLLER RESPONSE FROM ${request.servletPath}\n" +
                "PRINCIPAL: $principal\n" +
                "PARAM_AUTHENTICATION: $authentication\n" +
                "CONTEXT_AUTHENTICATION:${SecurityContextHolder.getContext().authentication}"
    }
}

@RestController
class ExampleController {

    @GetMapping(path = ["/open"])
    fun open() = "EXAMPLE CONTROLLER RESPONSE FROM /open"
}

