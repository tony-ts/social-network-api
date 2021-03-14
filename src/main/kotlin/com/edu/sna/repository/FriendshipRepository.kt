package com.edu.sna.repository

import com.edu.sna.model.Friendship
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface FriendshipRepository : ReactiveCrudRepository<Friendship, Long>, ManualFriendshipRepository