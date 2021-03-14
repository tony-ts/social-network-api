package com.edu.sna.web

import com.edu.sna.dto.LoginRequestDto
import com.edu.sna.dto.TokenInfo
import com.edu.sna.dto.SignupUserDto
import com.edu.sna.service.AuthService
import com.edu.sna.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    suspend fun signup(@RequestBody signupUserDto: SignupUserDto): ResponseEntity<Unit> =
        when (authService.signup(signupUserDto)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }


    @PostMapping("/login")
    suspend fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenInfo> {
        val accessToken = authService.login(loginRequestDto)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity.ok(TokenInfo(accessToken = accessToken))
    }
}
