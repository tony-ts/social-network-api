package com.edu.sna.dto

import com.edu.sna.model.User
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserDto(
    var id: Long?,
    @field:Email(regexp = ".+@.+\\..+")
    @field:NotBlank
    var email: String?,
    @field:NotBlank
    var firstName: String?,
    @field:NotBlank
    var lastName: String?,
    var birthdate: LocalDate?,
    @field:NotBlank
    var gender: String?,
    @field:NotBlank
    var interests: String?,
    @field:NotBlank
    var city: String?
) {
    companion object Factory {
        fun create(user: User) = UserDto(
            id = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            birthdate = user.birthdate,
            gender = user.gender,
            interests = user.interests,
            city = user.city
        )
    }
}
