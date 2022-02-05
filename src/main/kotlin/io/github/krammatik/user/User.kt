package io.github.krammatik.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val groups: List<String>
) {
    fun toAccount(password: String): Account {
        return Account(id, username, password, groups)
    }
}
