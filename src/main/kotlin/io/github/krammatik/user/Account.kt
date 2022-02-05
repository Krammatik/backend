package io.github.krammatik.user

import io.github.krammatik.dynamodb.ListType
import io.github.krammatik.dynamodb.Mappable
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

@Mappable
data class Account(
    var id: String = UUID.randomUUID().toString(),
    var username: String,
    var password: String,
    @ListType(String::class)
    var groups: List<String> = emptyList(),
) {

    companion object {
        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-512")
            val no = BigInteger(1, digest.digest(password.encodeToByteArray()))
            var hashtext: String = no.toString(16)
            while (hashtext.length < 128) {
                hashtext = "0$hashtext"
            }
            return hashtext
        }
    }

    fun toUser(): User {
        return User(id, username, groups)
    }

    fun passwordValid(password: String): Boolean {
        return hashPassword(password) == this.password
    }

}
