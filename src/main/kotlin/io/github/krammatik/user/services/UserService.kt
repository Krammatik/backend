package io.github.krammatik.user.services

import com.mongodb.client.MongoClient
import io.github.krammatik.plugins.ResourceAlreadyExistsException
import io.github.krammatik.user.Account
import io.github.krammatik.user.User
import org.kodein.di.DI
import org.kodein.di.instance
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

class UserService(di: DI) : IUserDatabase {

    private val mongoClient: MongoClient by di.instance()

    private val database get() = this.mongoClient.getDatabase("krammatik")
    private val userCollection get() = this.database.getCollection<Account>("users")

    override suspend fun createUser(user: User, password: String): User {
        if (getUserByName(user.username) != null) {
            throw ResourceAlreadyExistsException("User with username '${user.username}' already exists!")
        }
        this.userCollection.insertOne(user.toAccount(password))
        return user
    }

    override suspend fun getUsers(page: Int, count: Int): List<User> {
        val skip = (page.coerceAtLeast(1) * count) - count
        return this.userCollection.find().skip(skip).limit(count).map { it.toUser() }.toList()
    }

    override suspend fun getUserByName(name: String): User? {
        return getAccountByName(name)?.toUser()
    }

    override suspend fun getUserById(id: String): User? {
        return this.userCollection.findOne(Account::id eq id)?.toUser()
    }

    override suspend fun getAccountByName(username: String): Account? {
        return this.userCollection.findOne(Account::username eq username)
    }
}