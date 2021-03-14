package com.edu.sna.service.impl

import com.edu.sna.dto.FriendshipDto
import com.edu.sna.dto.UserFriendshipDto
import com.edu.sna.dto.PageableResponseDto
import com.edu.sna.dto.UserDto
import com.edu.sna.model.Friendship
import com.edu.sna.model.User
import com.edu.sna.repository.FriendshipRepository
import com.edu.sna.service.FriendshipService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class FriendshipServiceImpl(private val friendshipRepository: FriendshipRepository) : FriendshipService {

    override suspend fun sendFriendRequest(from: Long, to: Long): Boolean {
        val friendship = Friendship(
            fromUser = User(from),
            toUser = User(to),
            status = Friendship.FriendshipStatus.PENDING,
            createdDate = ZonedDateTime.now(),
            updatedDate = ZonedDateTime.now()
        )
        return friendshipRepository.saveManually(friendship)
    }

    override suspend fun findFriends(id: Long, pageRequest: PageRequest): PageableResponseDto<UserFriendshipDto> =
        friendshipRepository.findFriendsManually(id, pageRequest).let { page ->
            PageableResponseDto(
                page.totalElements,
                page.content.map {
                    val sourceUser = if (it.fromUser!!.id == id) it.toUser!! else it.fromUser!!
                    UserFriendshipDto(
                        id = it.id!!,
                        user = UserDto.create(sourceUser),
                        status = it.status!!,
                        createdDate = it.createdDate!!,
                        updatedDate = it.updatedDate!!
                    )
                }
            )
        }

    override suspend fun deleteFriend(currentUserId: Long, friendRecordId: Long): Boolean =
        friendshipRepository.deleteManually(currentUserId, friendRecordId)

    override suspend fun findStatus(currentUserId: Long, possibleFriendId: Long): FriendshipDto? =
        friendshipRepository.findStatusManually(currentUserId, possibleFriendId)?.let(FriendshipDto.Factory::create)

    override suspend fun accept(currentUserId: Long, friendRequestId: Long): Boolean =
        friendshipRepository.acceptManually(currentUserId, friendRequestId)

    override suspend fun findIncomingRequests(
        currentUserId: Long,
        pageRequest: PageRequest
    ): PageableResponseDto<UserFriendshipDto> =
        friendshipRepository.findIncomingRequestsManually(currentUserId, pageRequest).let { page ->
            PageableResponseDto(
                page.totalElements,
                page.content.map {
                    UserFriendshipDto(
                        id = it.id!!,
                        user = UserDto.create(it.fromUser!!),
                        status = it.status!!,
                        createdDate = it.createdDate!!,
                        updatedDate = it.updatedDate!!
                    )
                }
            )
        }

}