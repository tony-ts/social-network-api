package com.edu.sna.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table
data class User(
    @Id
    var id: Long?,
    var email: String?,
    var password: String?,
    var firstName: String?,
    var lastName: String?,
    var birthdate: LocalDate?,
    var gender: String?,
    var interests: String?,
    var city: String?
) {
    constructor(id: Long) : this(
        id,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}
