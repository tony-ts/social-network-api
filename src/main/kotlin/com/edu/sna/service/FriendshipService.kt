package com.edu.sna.service

import com.edu.sna.dto.FriendshipDto
import com.edu.sna.dto.UserFriendshipDto
import com.edu.sna.dto.PageableResponseDto
import org.springframework.data.domain.PageRequest

interface FriendshipService {
    suspend fun sendFriendRequest(from: Long, to: Long): Boolean
    suspend fun findFriends(id: Long, pageRequest: PageRequest): PageableResponseDto<UserFriendshipDto>
    suspend fun deleteFriend(currentUserId: Long, friendRecordId: Long): Boolean
    suspend fun findStatus(currentUserId: Long, possibleFriendId: Long): FriendshipDto?
    suspend fun accept(currentUserId: Long, friendRequestId: Long): Boolean
    suspend fun findIncomingRequests(
        currentUserId: Long,
        pageRequest: PageRequest
    ): PageableResponseDto<UserFriendshipDto>
}