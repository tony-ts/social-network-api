package com.edu.sna.service

import com.edu.sna.dto.PageableResponseDto
import com.edu.sna.dto.UserDto
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.PageRequest


interface UserService {

    suspend fun findById(id: Long): UserDto?

    suspend fun updateUser(userDto: UserDto): Boolean

    suspend fun findAllByName(requestedByUserId: Long, name: String, pageRequest: PageRequest): PageableResponseDto<UserDto>
}
