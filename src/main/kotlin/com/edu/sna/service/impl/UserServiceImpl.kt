package com.edu.sna.service.impl

import com.edu.sna.dto.PageableResponseDto
import com.edu.sna.dto.UserDto
import com.edu.sna.repository.UserRepository
import com.edu.sna.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {


    override suspend fun findById(id: Long): UserDto? =
        userRepository.findByIdManually(id)
            ?.run(UserDto.Factory::create)

    override suspend fun updateUser(userDto: UserDto): Boolean {
        userDto.id?.let { userId ->
            userRepository.findByIdManually(userId)
                ?.apply {
                    email = userDto.email
                    firstName = userDto.firstName
                    lastName = userDto.lastName
                    birthdate = userDto.birthdate
                    gender = userDto.gender
                    interests = userDto.interests
                    city = userDto.city
                }?.let {
                    userRepository.updateManually(it)
                }
        }
        return false
    }

    override suspend fun findAllByName(requestedByUserId: Long, name: String, pageRequest: PageRequest): PageableResponseDto<UserDto> =
        userRepository.findAllByNameManually(requestedByUserId, name, pageRequest).let { page ->
            PageableResponseDto(
                page.totalElements,
                page.content.map(UserDto.Factory::create)
            )
        }


}
