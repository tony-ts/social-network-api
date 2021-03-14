package com.edu.sna.repository

import com.edu.sna.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface ManualUserRepository {

    suspend fun saveManually(user: User): Boolean

    suspend fun updateManually(user: User): Boolean

    suspend fun findByIdManually(id: Long): User?

    suspend fun findByEmailManually(email: String): User?

    suspend fun findAllByNameManually(requestedByUserId: Long, name: String, pageRequest: PageRequest): Page<User>

}