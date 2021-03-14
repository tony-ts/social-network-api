package com.edu.sna.service.impl

import com.edu.sna.dto.LoginRequestDto
import com.edu.sna.dto.SignupUserDto
import com.edu.sna.model.AuthUser
import com.edu.sna.model.User
import com.edu.sna.repository.UserRepository
import com.edu.sna.service.JwtService
import com.edu.sna.service.AuthService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    private val log: Logger = LogManager.getLogger(AuthServiceImpl::class.java)

    override suspend fun signup(signupUserDto: SignupUserDto): Boolean =
        userRepository.saveManually(
            User(
                id = null,
                email = signupUserDto.email,
                password = passwordEncoder.encode(signupUserDto.password),
                firstName = signupUserDto.firstName,
                lastName = signupUserDto.lastName,
                birthdate = signupUserDto.birthdate,
                gender = signupUserDto.gender,
                interests = signupUserDto.interests,
                city = signupUserDto.city
            )
        )

    override suspend fun login(loginRequestDto: LoginRequestDto): String? =
        loginRequestDto.email?.let {
            userRepository.findByEmailManually(it)
        }?.let {
            log.debug("found user by email ${it.email} to check password")
            Pair(it, AuthUser(username = it.email!!, password = it.password!!))
        }?.let {
            if (passwordEncoder.matches(loginRequestDto.password, it.second.password)) {
                jwtService.createJwt(it.first.id!!, it.second)
            } else {
                log.debug("password doesn't match for username - ${it.second.username}")
                null
            }
        }
}
