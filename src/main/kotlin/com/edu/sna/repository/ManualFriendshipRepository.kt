package com.edu.sna.repository

import com.edu.sna.model.Friendship
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface ManualFriendshipRepository {
    suspend fun saveManually(friendship: Friendship): Boolean

    suspend fun findFriendsManually(id: Long, pageRequest: PageRequest): Page<Friendship>

    suspend fun deleteManually(currentUserId: Long, friendRecordId: Long): Boolean

    suspend fun findStatusManually(currentUserId: Long, possibleFriendId: Long): Friendship?

    suspend fun acceptManually(currentUserId: Long, friendRequestId: Long): Boolean

    suspend fun findIncomingRequestsManually(currentUserId: Long, pageRequest: PageRequest): Page<Friendship>
}