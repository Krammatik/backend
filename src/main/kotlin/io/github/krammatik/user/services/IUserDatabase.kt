package io.github.krammatik.user.services

import io.github.krammatik.models.User
import io.github.krammatik.user.dto.UserDto

interface IUserDatabase {

    suspend fun createUser(user: UserDto, password: String): User

    suspend fun getUsers(page: Int, count: Int): List<User>

    suspend fun getUserByName(name: String) : User?

    suspend fun getUserById(id: String): User?

    suspend fun getAccountByName(username: String): User?
}