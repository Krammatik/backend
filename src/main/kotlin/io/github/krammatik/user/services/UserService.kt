package io.github.krammatik.user.services

import com.mongodb.client.MongoClient
import io.github.krammatik.models.User
import io.github.krammatik.plugins.ResourceAlreadyExistsException
import io.github.krammatik.user.dto.UserDto
import org.kodein.di.DI
import org.kodein.di.instance
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.util.UUID

class UserService(val di: DI) : IUserDatabase {

    private val mongoClient: MongoClient by di.instance()

    private val database get() = this.mongoClient.getDatabase("krammatik")
    private val userCollection get() = this.database.getCollection<User>("users")

    override suspend fun createUser(user: UserDto, password: String): User {
        if (getUserByName(user.username) != null) {
            throw ResourceAlreadyExistsException("User with username '${user.username}' already exists!")
        }
        val dbUser = user.toTransferable(di).apply {
            this.id = UUID.randomUUID().toString()
            this.password = password
        }
        this.userCollection.insertOne(dbUser)
        return dbUser
    }

    override suspend fun getUsers(page: Int, count: Int): List<User> {
        val skip = (page.coerceAtLeast(1) * count) - count
        return this.userCollection.find().skip(skip).limit(count).toList()
    }

    override suspend fun getUserByName(name: String): User? {
        return getAccountByName(name)
    }

    override suspend fun getUserById(id: String): User? {
        return this.userCollection.findOne(User::id eq id)
    }

    override suspend fun getAccountByName(username: String): User? {
        return this.userCollection.findOne(User::username eq username)
    }
}