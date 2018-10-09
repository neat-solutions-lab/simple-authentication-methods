package nsl.sam.method.basicauth.usersimporter

import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.interfaces.Resettable
import java.io.Closeable

interface UsersImporter :
        Closeable, Resettable, ItemsAvailabilityAware, Iterator<Triple<String, String, Array<String>>>