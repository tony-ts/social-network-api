package com.edu.sna.service.impl

import com.edu.sna.configuration.ApplicationProperties
import com.edu.sna.dto.TokenInfo
import com.edu.sna.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtServiceImpl(private val applicationProperties: ApplicationProperties) : JwtService {

    private val jwtParser = Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(applicationProperties.jwtProperties.secret.toByteArray()))
        .build()


    override fun createJwt(userId: Long, userDetails: UserDetails): String {
        val claims = mutableMapOf<String, Any>()
        claims["userId"] = userId
        claims["authorities"] = userDetails.authorities
        claims["enabled"] = userDetails.isEnabled

        val (secret, expiration) = applicationProperties.jwtProperties

        val key = Keys.hmacShaKeyFor(secret.toByteArray())
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(expiration)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    override fun validateJwt(jwt: String): Jws<Claims> = jwtParser.parseClaimsJws(jwt)
}
