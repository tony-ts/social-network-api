package com.edu.sna.repository

import com.edu.sna.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository : ReactiveCrudRepository<User, Long>, ManualUserRepository