package nsl.sam.method.token.tokensimporter

import nsl.sam.interfaces.ItemsAvailabilityAware
import nsl.sam.interfaces.Resettable
import nsl.sam.method.token.token.ResolvedToken
import java.io.Closeable

interface TokensImporter : Closeable, Resettable, ItemsAvailabilityAware, Iterator<ResolvedToken>