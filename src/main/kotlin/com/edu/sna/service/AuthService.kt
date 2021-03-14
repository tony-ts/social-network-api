package com.edu.sna.service

import com.edu.sna.dto.LoginRequestDto
import com.edu.sna.dto.SignupUserDto

interface AuthService {

    /**
     * Performs the registration of a new user
     * @param signupUserDto information provided by the user
     * @return true if successful, otherwise false
     * */
    suspend fun signup(signupUserDto: SignupUserDto): Boolean

    /**
     * Performs user authentication by verifying the information provided
     * @param loginRequestDto information provided by the user
     * @return access_token as jwt string or null if error happens
     * */
    suspend fun login(loginRequestDto: LoginRequestDto): String?


}