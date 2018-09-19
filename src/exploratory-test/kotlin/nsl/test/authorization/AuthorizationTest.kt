package nsl.test.authorization

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.web.context.WebApplicationContext

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableWebSecurity
class AuthorizationTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext


    @Test
    fun test() {
        try {
            webApplicationContext.getBean("roleVoter")
            println("roleVoter FOUND")
        } catch (e:Exception) {
            println("roleVoter NOT FOUND")
        }


        try {
            webApplicationContext.getBean(RoleVoter::class.java)
            println("${RoleVoter::class.qualifiedName} FOUND")
        } catch (e:Exception){
            println("${RoleVoter::class.qualifiedName} NOT FOUND")
        }



    }

}

@Configuration
class Configurator: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().anyRequest().permitAll()
        http.authorizeRequests().antMatchers("/not-reachable/**").hasRole("ADMIN")
    }
}