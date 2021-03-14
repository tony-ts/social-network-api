package com.edu.sna.dto

import com.edu.sna.model.Friendship
import com.edu.sna.model.User
import java.time.ZonedDateTime

data class FriendshipDto(
    val id: Long,
    val fromUser: Long,
    val toUser: Long,
    var status: Friendship.FriendshipStatus,
    var createdDate: ZonedDateTime,
    var updatedDate: ZonedDateTime
) {
    companion object Factory {
        fun create(friendship: Friendship) = FriendshipDto(
            id = friendship.id!!,
            fromUser = friendship.fromUser!!.id!!,
            toUser = friendship.toUser!!.id!!,
            status = friendship.status!!,
            createdDate = friendship.createdDate!!,
            updatedDate = friendship.updatedDate!!
        )
    }
}