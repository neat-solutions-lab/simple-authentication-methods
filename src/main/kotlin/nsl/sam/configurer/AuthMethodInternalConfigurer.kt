package nsl.sam.configurer

import nsl.sam.availability.AvailabilityAware
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity

/**
 * All implementations of this interface are to be found by [nsl.sms.api.auth.config.WebSecurityConfiguration]
 * which uses this interface's configure() method to configure new authentication method.
 */
interface AuthMethodInternalConfigurer: AvailabilityAware {
    /**
     * Checks if given authentication method is active. It can be inactive when for example it could not find
     * configuration files or something like this.
     */
    //fun isActive(): Boolean

    /**
     * Called by [nsl.sms.api.auth.config.WebSecurityConfiguration] to configure particular authentication method.
     */
    fun configure(http: HttpSecurity):HttpSecurity

    fun configure(auth: AuthenticationManagerBuilder)

    /**
     * Useful for logging/debugging purposes.
     */
    fun methodName():String
}