package com.edu.sna.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.core.userdetails.UserDetails

interface JwtService {

    /**
     * Builds the JWT and serializes it to a compact, URL-safe string.
     * @param userDetails authenticated user information.
     * @return a compact URL-safe JWT string.
     *
     * */
    fun createJwt(userId: Long, userDetails: UserDetails): String

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     * @param jwt to validate
     * @return jws claims
     */
    fun validateJwt(jwt: String): Jws<Claims>

}