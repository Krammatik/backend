package io.github.krammatik.user

interface IUserDatabase {

    suspend fun createUser(user: User, password: String): User

    suspend fun getUserById(id: String): User?

    suspend fun getAccountByName(username: String): Account?
}