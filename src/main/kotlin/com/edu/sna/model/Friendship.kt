package com.edu.sna.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Table
data class Friendship(
    @Id
    var id: Long? = null,
    var fromUser: User?,
    var toUser: User?,
    var status: FriendshipStatus? = FriendshipStatus.PENDING,
    @CreatedDate
    var createdDate: ZonedDateTime?,
    @LastModifiedDate
    var updatedDate: ZonedDateTime?
) {

    enum class FriendshipStatus {
        /**
         * Pending confirmation by the recipient
         * */
        PENDING,

        /**
         * Received confirmation by the recipient
         * */
        ACCEPTED
    }
}