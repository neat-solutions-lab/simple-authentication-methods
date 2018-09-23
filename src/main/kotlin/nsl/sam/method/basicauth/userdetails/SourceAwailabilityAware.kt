package nsl.sam.method.basicauth.userdetails

import org.springframework.security.core.userdetails.UserDetailsService

interface SourceAwailabilityAware: UserDetailsService {

    fun isSourceAvailable(): Boolean

}