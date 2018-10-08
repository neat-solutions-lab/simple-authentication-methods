package nsl.sam.method.basicauth.userdetails.importer

import nsl.sam.availability.ItemsAvailabilityAware
import java.io.Closeable

interface UsersImporter: Closeable, Resetable, ItemsAvailabilityAware, Iterator<Triple<String, String, Array<String>>> {

}