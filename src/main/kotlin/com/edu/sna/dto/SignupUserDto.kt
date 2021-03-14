package com.edu.sna.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank


data class SignupUserDto(
    var id: Long?,
    @field:Email(regexp = ".+@.+\\..+")
    @field:NotBlank
    var email: String?,
    @field:NotBlank
    @field:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String?,
    @field:NotBlank
    var firstName: String?,
    @field:NotBlank
    var lastName: String?,
    var birthdate: LocalDate?,
    @field:NotBlank
    var gender: String?,
    var interests: String?,
    @field:NotBlank
    var city: String?
)
