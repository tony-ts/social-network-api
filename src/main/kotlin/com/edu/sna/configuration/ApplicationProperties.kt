package com.edu.sna.configuration

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
data class ApplicationProperties(
    val corsProperties: CorsEndpointProperties = CorsEndpointProperties(),
    val jwtProperties: JwtProperties
) {

    data class JwtProperties(
        val secret: String,
        val expiration: Duration
    )
}

