package com.edu.sna.dto

import javax.validation.constraints.NotBlank

data class LoginRequestDto(
    @field:NotBlank
    val email: String?,

    @field:NotBlank
    val password: String?
)