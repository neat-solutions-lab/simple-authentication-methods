package nsl.sam.method.token.tokensresolver

import nsl.sam.core.annotation.EnableAnnotationAttributes
import org.springframework.core.env.Environment

interface TokensResolverFactory {
    fun create(attributes: EnableAnnotationAttributes, environment: Environment): TokensResolver
}