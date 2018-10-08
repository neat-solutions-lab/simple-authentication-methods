package nsl.sam.method.basicauth.userdetails.importer

import java.io.Closeable

interface UsersImporter: Closeable, Resetable, AvailabilityAware, Iterator<Triple<String, String, Array<String>>> {

}