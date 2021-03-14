package com.edu.sna.web

import com.edu.sna.configuration.CurrentUserId
import com.edu.sna.dto.FriendshipDto
import com.edu.sna.dto.PageableResponseDto
import com.edu.sna.dto.UserFriendshipDto
import com.edu.sna.service.FriendshipService
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@RequestMapping("/api/users")
class UserFriendsController(
    private val friendshipService: FriendshipService
) {

    @GetMapping("/{id}/friends")
    suspend fun findFriends(
        @PathVariable id: Long,
        @RequestParam @PositiveOrZero page: Int,
        @RequestParam @Positive size: Int
    ): PageableResponseDto<UserFriendshipDto> =
        friendshipService.findFriends(id, PageRequest.of(page, size))


    @GetMapping("/me/friends")
    suspend fun findCurrentUserFriends(
        @CurrentUserId id: Mono<Long>,
        @RequestParam @PositiveOrZero page: Int,
        @RequestParam @Positive size: Int
    ): PageableResponseDto<UserFriendshipDto> =
        findFriends(id.awaitFirst(), page, size)

    @PostMapping("/{friendId}/friend-request")
    suspend fun sendFriendRequest(
        @CurrentUserId currentUserId: Mono<Long>,
        @PathVariable friendId: Long
    ): ResponseEntity<Unit> =
        when (friendshipService.sendFriendRequest(currentUserId.awaitFirst(), friendId)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    @DeleteMapping("/me/friends/{friendRecordId}")
    suspend fun deleteFriend(
        @CurrentUserId currentUserId: Mono<Long>,
        @PathVariable friendRecordId: Long
    ): ResponseEntity<Unit> =
        when (friendshipService.deleteFriend(currentUserId.awaitFirst(), friendRecordId)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    @GetMapping("/me/friends/status")
    suspend fun findFriendship(
        @CurrentUserId currentUserId: Mono<Long>,
        @RequestParam userId: Long
    ): FriendshipDto? =
        friendshipService.findStatus(currentUserId.awaitFirst(), userId)

    @PostMapping("/me/friend-request/{friendRequestId}/accept")
    suspend fun acceptFriendRequest(
        @CurrentUserId currentUserId: Mono<Long>,
        @PathVariable friendRequestId: Long
    ): ResponseEntity<Unit> =
        when (friendshipService.accept(currentUserId.awaitFirst(), friendRequestId)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    @GetMapping("/me/friends/incoming")
    suspend fun findIncomingRequests(
        @CurrentUserId currentUserId: Mono<Long>,
        @RequestParam @PositiveOrZero page: Int,
        @RequestParam @Positive size: Int
    ): PageableResponseDto<UserFriendshipDto> =
        friendshipService.findIncomingRequests(currentUserId.awaitSingle(), PageRequest.of(page, size))
}