package com.edu.sna.configuration

import com.edu.sna.model.AuthenticatedUser
import com.edu.sna.service.JwtService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(private val jwtService: JwtService) : ReactiveAuthenticationManager {
    private val log: Logger = LogManager.getLogger(JwtAuthenticationManager::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return try {
            val claims = jwtService.validateJwt(authentication.credentials.toString())
            val token = UsernamePasswordAuthenticationToken(
                AuthenticatedUser(
                    id = claims.body["userId", Integer::class.java].toLong(),
                    email = claims.body.subject
                ),
                authentication.credentials.toString(),
                claims.body.get("authorities", List::class.java)
                    .map { authority -> authority as Map<String, String> }
                    .map { SimpleGrantedAuthority(it.getValue("authority")) }
            )
            Mono.just(token)
        } catch (e: Exception) {
            log.error("Jwt validate error!", e)
            Mono.error { ResponseStatusException(HttpStatus.UNAUTHORIZED) }
        }
    }
}
