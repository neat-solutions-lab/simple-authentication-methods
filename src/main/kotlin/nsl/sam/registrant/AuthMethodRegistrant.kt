package nsl.sam.registrant

import org.springframework.security.config.annotation.web.builders.HttpSecurity

/**
 * All implementations of this interface are found by [nsl.sms.api.auth.config.WebSecurityConfiguration]
 * which uses this interface's register() method to register new authentication method.
 */
interface AuthMethodRegistrant {
    /**
     * Checks if given authentication method is active. It can be inactive when for example it could not find
     * configuration files or something like this.
     */
    fun isActive(): Boolean

    /**
     * Called by [nsl.sms.api.auth.config.WebSecurityConfiguration] to register particular authentication method.
     */
    fun register(http: HttpSecurity):HttpSecurity

    /**
     * Useful for logging/debugging purposes.
     */
    fun methodName():String
}