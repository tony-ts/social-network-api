package com.edu.sna.configuration

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurerComposite


@Configuration
class WebFluxConfig {

    @Bean
    fun corsConfigurer(applicationProperties: ApplicationProperties): WebFluxConfigurer {
        return object : WebFluxConfigurerComposite() {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(*applicationProperties.corsProperties.allowedOrigins.toTypedArray())
                    .allowedMethods(*applicationProperties.corsProperties.allowedMethods.toTypedArray())
                    .allowCredentials(applicationProperties.corsProperties.allowCredentials ?: false)
                    .maxAge(applicationProperties.corsProperties.maxAge.seconds)
            }
        }
    }

    @Bean
    @ConfigurationProperties("application.cors-properties")
    fun corsProperties(): CorsEndpointProperties {
        return CorsEndpointProperties()
    }
}
