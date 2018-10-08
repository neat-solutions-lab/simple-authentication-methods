package nsl.sam.method.basicauth.usersimporter

import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.interfaces.Resetable
import java.io.Closeable

interface UsersImporter:
        Closeable, Resetable, ItemsAvailabilityAware, Iterator<Triple<String, String, Array<String>>>