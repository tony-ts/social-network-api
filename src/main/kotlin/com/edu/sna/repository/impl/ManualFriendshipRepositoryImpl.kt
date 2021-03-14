package com.edu.sna.repository.impl

import com.edu.sna.model.Friendship
import com.edu.sna.model.TotalResponse
import com.edu.sna.model.User
import com.edu.sna.repository.ManualFriendshipRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull
import java.time.LocalDate
import java.time.ZonedDateTime

class ManualFriendshipRepositoryImpl(private val client: DatabaseClient) : ManualFriendshipRepository {

    override suspend fun saveManually(friendship: Friendship): Boolean {
        val sql = """
                INSERT INTO FRIENDSHIP(from_user, to_user, status, created_date, updated_date)
                VALUES(:fromUser, :toUser, :status, :createdDate, :updatedDate)
            """
        return client.sql(sql)
            .bind("fromUser", friendship.fromUser!!.id, Long::class.java)
            .bind("toUser", friendship.toUser!!.id, Long::class.java)
            .bind("status", friendship.status!!.name, String::class.java)
            .bind("createdDate", friendship.createdDate, ZonedDateTime::class.java)
            .bind("updatedDate", friendship.updatedDate, ZonedDateTime::class.java)
            .fetch().rowsUpdated().awaitFirst() == 1
    }

    override suspend fun findFriendsManually(id: Long, pageRequest: PageRequest): Page<Friendship> {
        val sql = """
                SELECT found_rows() total, f.id, f.status, f.created_date, f.updated_date,
                       uf.id uf_id, uf.email uf_email, uf.password uf_password, uf.first_name uf_first_name, uf.last_name uf_last_name,
                       uf.birthdate uf_birthdate, uf.gender uf_gender, uf.interests uf_interests, uf.city uf_city,
                       ut.id ut_id, ut.email ut_email, ut.password ut_password, ut.first_name ut_first_name, ut.last_name ut_last_name,
                       ut.birthdate ut_birthdate, ut.gender ut_gender, ut.interests ut_interests, ut.city ut_city
                FROM
                FRIENDSHIP f
                JOIN USER uf on uf.ID = f.FROM_USER
                JOIN USER ut on ut.ID = f.TO_USER
                WHERE (f.FROM_USER = :id OR f.TO_USER = :id)
                AND f.STATUS = '${Friendship.FriendshipStatus.ACCEPTED}'
                ORDER BY f.ID
                LIMIT :limit
                OFFSET :offset
            """
        return client.sql(sql)
            .bind("id", id)
            .bind("limit", pageRequest.pageSize)
            .bind("offset", pageRequest.offset)
            .map { row ->
                TotalResponse(
                    total = row.get("total", Long::class.java)!!,
                    entity = Friendship(
                        id = row.get("id", Long::class.java),
                        fromUser = User(
                            id = row.get("uf_id", Long::class.java),
                            email = row.get("uf_email", String::class.java),
                            password = row.get("uf_password", String::class.java),
                            firstName = row.get("uf_first_name", String::class.java),
                            lastName = row.get("uf_last_name", String::class.java),
                            birthdate = row.get("uf_birthdate", LocalDate::class.java),
                            gender = row.get("uf_gender", String::class.java),
                            interests = row.get("uf_interests", String::class.java),
                            city = row.get("uf_city", String::class.java)
                        ),
                        toUser = User(
                            id = row.get("ut_id", Long::class.java),
                            email = row.get("ut_email", String::class.java),
                            password = row.get("ut_password", String::class.java),
                            firstName = row.get("ut_first_name", String::class.java),
                            lastName = row.get("ut_last_name", String::class.java),
                            birthdate = row.get("ut_birthdate", LocalDate::class.java),
                            gender = row.get("ut_gender", String::class.java),
                            interests = row.get("ut_interests", String::class.java),
                            city = row.get("ut_city", String::class.java)
                        ),
                        status = row.get("status", String::class.java)!!
                            .let { Friendship.FriendshipStatus.valueOf(it) },
                        createdDate = row.get("created_date", ZonedDateTime::class.java),
                        updatedDate = row.get("updated_date", ZonedDateTime::class.java)
                    )
                )
            }
            .all()
            .collectList()
            .map { it.wrapPage(pageRequest) }
            .awaitSingle()
    }

    override suspend fun deleteManually(currentUserId: Long, friendRecordId: Long): Boolean {
        val sql = """
                DELETE FROM FRIENDSHIP
                WHERE (from_user = :currentUserId OR to_user = :currentUserId)
                AND id = :friendRecordId 
            """
        return client.sql(sql)
            .bind("currentUserId", currentUserId)
            .bind("friendRecordId", friendRecordId)
            .fetch().rowsUpdated().awaitFirst() == 1
    }

    override suspend fun findStatusManually(currentUserId: Long, possibleFriendId: Long): Friendship? {
        val sql = """
            SELECT id, from_user, to_user, status, created_date, updated_date FROM  FRIENDSHIP
            WHERE (from_user = :currentUserId AND to_user = :possibleFriendId)
            OR (from_user = :possibleFriendId AND to_user = :currentUserId)
        """
        return client.sql(sql)
            .bind("currentUserId", currentUserId)
            .bind("possibleFriendId", possibleFriendId)
            .map { row ->
                Friendship(
                    id = row.get("id", Long::class.java),
                    fromUser = User(id = row.get("from_user", Long::class.java)!!),
                    toUser = User(id = row.get("to_user", Long::class.java)!!),
                    status = row.get("status", String::class.java)!!.let { Friendship.FriendshipStatus.valueOf(it) },
                    createdDate = row.get("created_date", ZonedDateTime::class.java),
                    updatedDate = row.get("updated_date", ZonedDateTime::class.java)
                )
            }
            .awaitOneOrNull()
    }

    override suspend fun acceptManually(currentUserId: Long, friendRequestId: Long): Boolean {
        val sql = """
            UPDATE FRIENDSHIP SET STATUS = '${Friendship.FriendshipStatus.ACCEPTED}'
            WHERE TO_USER = :currentUserId
            AND ID = :friendRequestId
            AND STATUS = '${Friendship.FriendshipStatus.PENDING}'
        """
        return client.sql(sql)
            .bind("currentUserId", currentUserId)
            .bind("friendRequestId", friendRequestId)
            .fetch().rowsUpdated().awaitSingle() == 1
    }

    override suspend fun findIncomingRequestsManually(currentUserId: Long, pageRequest: PageRequest): Page<Friendship> {
        val sql = """
            select FOUND_ROWS() total, f.id, f.status, f.created_date, f.updated_date,
                    uf.id uf_id, uf.email uf_email, uf.password uf_password, uf.first_name uf_first_name, uf.last_name uf_last_name,
                    uf.birthdate uf_birthdate, uf.gender uf_gender, uf.interests uf_interests, uf.city uf_city
            from FRIENDSHIP f
                     JOIN USER uf on uf.ID = f.FROM_USER
            where f.TO_USER = :currentUserId
            and f.STATUS = '${Friendship.FriendshipStatus.PENDING}'
            ORDER BY f.id
            LIMIT :limit
            OFFSET :offset
            """
        return client.sql(sql)
            .bind("currentUserId", currentUserId)
            .bind("limit", pageRequest.pageSize)
            .bind("offset", pageRequest.offset)
            .map { row ->
                TotalResponse(
                    total = row.get("total", Long::class.java)!!,
                    entity = Friendship(
                        id = row.get("id", Long::class.java),
                        fromUser = User(
                            id = row.get("uf_id", Long::class.java),
                            email = row.get("uf_email", String::class.java),
                            password = row.get("uf_password", String::class.java),
                            firstName = row.get("uf_first_name", String::class.java),
                            lastName = row.get("uf_last_name", String::class.java),
                            birthdate = row.get("uf_birthdate", LocalDate::class.java),
                            gender = row.get("uf_gender", String::class.java),
                            interests = row.get("uf_interests", String::class.java),
                            city = row.get("uf_city", String::class.java)
                        ),
                        toUser = null,
                        status = row.get("status", String::class.java)!!
                            .let { Friendship.FriendshipStatus.valueOf(it) },
                        createdDate = row.get("created_date", ZonedDateTime::class.java),
                        updatedDate = row.get("updated_date", ZonedDateTime::class.java)
                    )
                )
            }
            .all()
            .collectList()
            .map { it.wrapPage(pageRequest) }
            .awaitSingle()
    }
}