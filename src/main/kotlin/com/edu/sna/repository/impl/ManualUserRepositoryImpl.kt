package com.edu.sna.repository.impl

import com.edu.sna.model.TotalResponse
import com.edu.sna.model.User
import com.edu.sna.repository.ManualUserRepository
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull
import java.time.LocalDate

class ManualUserRepositoryImpl(private val client: DatabaseClient) : ManualUserRepository {

    override suspend fun saveManually(user: User): Boolean {
        val sql = """
                INSERT INTO USER(email, password, first_name, last_name, birthdate, gender, interests, city)
                VALUES(:email, :password, :firstName, :lastName, :birthdate, :gender, :interests, :city)
            """
        return client.sql(sql)
            .bind("email", user.email, String::class.java)
            .bind("password", user.password, String::class.java)
            .bind("firstName", user.firstName, String::class.java)
            .bind("lastName", user.lastName, String::class.java)
            .bind("birthdate", user.birthdate, LocalDate::class.java)
            .bind("gender", user.gender, String::class.java)
            .bind("interests", user.interests, String::class.java)
            .bind("city", user.city, String::class.java)
            .fetch().rowsUpdated().awaitFirst() == 1
    }

    override suspend fun updateManually(user: User): Boolean {
        val sql = """
                UPDATE USER
                SET EMAIL      = :email,
                    FIRST_NAME = :firstName,
                    LAST_NAME  = :lastName,
                    BIRTHDATE  = :birthdate,
                    GENDER     = :gender,
                    INTERESTS  = :interests,
                    CITY       = :city
                WHERE ID = :id
            """
        return client.sql(sql)
            .bind("id", user.id, Long::class.java)
            .bind("email", user.email, String::class.java)
            .bind("firstName", user.firstName, String::class.java)
            .bind("lastName", user.lastName, String::class.java)
            .bind("birthdate", user.birthdate, LocalDate::class.java)
            .bind("gender", user.gender, String::class.java)
            .bind("interests", user.interests, String::class.java)
            .bind("city", user.city, String::class.java)
            .fetch().rowsUpdated().awaitFirst() == 1
    }

    override suspend fun findByIdManually(id: Long): User? {
        val sql = """
                    select id, email, password, first_name, last_name,
                           birthdate, gender, interests, city
                    from USER
                    where id = :id
                    """
        return client.sql(sql)
            .bind("id", id)
            .map(::mapRowToEntity)
            .awaitOneOrNull()
    }

    override suspend fun findByEmailManually(email: String): User? {
        val sql = """
                SELECT id, email, password, first_name, last_name,
                       birthdate, gender, interests, city
                FROM USER
                WHERE email = :email
            """
        return client.sql(sql)
            .bind("email", email)
            .map(::mapRowToEntity)
            .awaitOneOrNull()
    }

    override suspend fun findAllByNameManually(
        requestedByUserId: Long,
        name: String,
        pageRequest: PageRequest
    ): Page<User> {
        val sql = """
                SELECT found_rows() total, u.id, email, password, first_name, last_name,
                       birthdate, gender, interests, city
                FROM USER u
                         LEFT JOIN FRIENDSHIP f1 on u.id = f1.FROM_USER and f1.TO_USER = :requestedByUserId
                         LEFT JOIN FRIENDSHIP f2 on u.id = f2.TO_USER and f2.FROM_USER = :requestedByUserId
                WHERE (lower(first_name) like concat(:name, '%')
                    OR lower(last_name) like concat(:name, '%')
                    OR lower(concat(first_name, ' ', last_name)) like concat(:name, '%')
                    )
                  AND u.id <> :requestedByUserId
                  AND (f1.id is null AND f2.id is null)
                ORDER BY u.ID
                LIMIT :limit
                OFFSET :offset
            """
        return client.sql(sql)
            .bind("requestedByUserId", requestedByUserId)
            .bind("name", name)
            .bind("limit", pageRequest.pageSize)
            .bind("offset", pageRequest.offset)
            .map { row -> TotalResponse(row.get("total", Long::class.java)!!, mapRowToEntity(row)) }
            .all()
            .collectList()
            .map { it.wrapPage(pageRequest) }
            .awaitSingle()
    }

    private fun mapRowToEntity(row: Row) = User(
        id = row.get("id", Long::class.java),
        email = row.get("email", String::class.java),
        password = row.get("password", String::class.java),
        firstName = row.get("first_name", String::class.java),
        lastName = row.get("last_name", String::class.java),
        birthdate = row.get("birthdate", LocalDate::class.java),
        gender = row.get("gender", String::class.java),
        interests = row.get("interests", String::class.java),
        city = row.get("city", String::class.java)
    )
}
