package io.github.krammatik.user.services

import io.github.krammatik.user.Account
import io.github.krammatik.user.User

interface IUserDatabase {

    suspend fun createUser(user: User, password: String): User

    suspend fun getUsers(page: Int, count: Int): List<User>

    suspend fun getUserByName(name: String) : User?

    suspend fun getUserById(id: String): User?

    suspend fun getAccountByName(username: String): Account?
}