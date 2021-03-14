package com.edu.sna.web

import com.edu.sna.configuration.CurrentUserId
import com.edu.sna.dto.PageableResponseDto
import com.edu.sna.dto.UserDto
import com.edu.sna.service.UserService
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{id}")
    suspend fun findUser(@PathVariable id: Long): UserDto? =
        userService.findById(id)

    @GetMapping("/me")
    suspend fun findCurrentUser(@CurrentUserId id: Mono<Long>): UserDto? =
        findUser(id.awaitFirst())

    @PutMapping("/me")
    suspend fun updateCurrentUser(
        @CurrentUserId id: Mono<Long>,
        @RequestBody userDto: UserDto
    ): ResponseEntity<Unit> {
        userDto.id = id.awaitFirst()
        userService.updateUser(userDto)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    suspend fun findAllUsersByName(
        @CurrentUserId id: Mono<Long>,
        @RequestParam name: String,
        @RequestParam @PositiveOrZero page: Int,
        @RequestParam @Positive size: Int
    ): PageableResponseDto<UserDto> =
        userService.findAllByName(id.awaitFirst(), name, PageRequest.of(page, size))
}
