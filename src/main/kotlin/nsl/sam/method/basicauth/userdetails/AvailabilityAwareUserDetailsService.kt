package nsl.sam.method.basicauth.userdetails

import nsl.sam.interfaces.ItemsAvailabilityAware
import org.springframework.security.core.userdetails.UserDetailsService

interface AvailabilityAwareUserDetailsService : UserDetailsService, ItemsAvailabilityAware