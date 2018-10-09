package nsl.test.multiauthmanagers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.cast

@SpringBootApplication
@EnableWebSecurity(debug = true)
class MultiAuthManagerTest {

//    @Bean
//    fun userDetails(): UserDetailsService {
//        val users = User.withDefaultPasswordEncoder()
//        val manager = InMemoryUserDetailsManager()
//        manager.createUser(users.username("user").password("password").roles("USER").build())
//        manager.createUser(users.username("admin").password("password").roles("USER","ADMIN").build())
//        return manager
//    }

}

fun main(args: Array<String>) {
    SpringApplication.run(MultiAuthManagerTest::class.java)
}

@Configuration
@Order
class SecurityConfigurationOne: WebSecurityConfigurerAdapter() {


    @Suppress("DEPRECATION")
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user-one").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER", "ADMIN")
    }



    override fun configure(http: HttpSecurity) {
        http
                .antMatcher("/one/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .httpBasic()
    }

}

@Configuration
@Order(20)
class SecurityConfigurationTwo: WebSecurityConfigurerAdapter() {

    @Suppress("DEPRECATION")
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user-two").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER", "ADMIN")
    }


    override fun configure(web: WebSecurity) {

    }

    override fun configure(http: HttpSecurity) {
        http
                .antMatcher("/two/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .httpBasic()
    }

}

@RestController
class ControllerOne {

    @GetMapping(path = ["/one"])
    fun entryPoint() = "Hello form controller ONE."

}

@RestController
class ControllerTwo {

    @GetMapping(path = ["/two"])
    fun entryPoint() = "Hello form controller TWO."

}

@RestController
class ControllerThree {

    @GetMapping(path = ["/three"])
    fun entryPoint() = "Hello form controller THREE."

}

@RestController
class CloseController {

    @Autowired
    lateinit var appCtx: ApplicationContext

    @GetMapping(path = ["/close"])
    fun closeApp():String {
        ConfigurableApplicationContext::class.cast(appCtx).close()
        return "Closing ..."
    }

}
