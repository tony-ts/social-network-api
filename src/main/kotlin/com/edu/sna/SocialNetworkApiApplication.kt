package com.edu.sna

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SocialNetworkApiApplication

fun main(args: Array<String>) {
    runApplication<SocialNetworkApiApplication>(*args)
}
