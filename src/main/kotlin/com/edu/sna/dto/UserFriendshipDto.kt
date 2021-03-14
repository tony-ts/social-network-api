package com.edu.sna.dto

import com.edu.sna.model.Friendship
import java.time.ZonedDateTime

data class UserFriendshipDto(
    val id: Long,
    val user: UserDto,
    var status: Friendship.FriendshipStatus,
    var createdDate: ZonedDateTime,
    var updatedDate: ZonedDateTime
)
