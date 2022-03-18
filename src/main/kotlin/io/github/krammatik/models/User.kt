package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.user.dto.UserDto
import org.bson.codecs.pojo.annotations.BsonId
import org.kodein.di.DI
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

data class User(
    @BsonId
    var id: String,
    var username: String,
    var password: String,
    var groups: List<String> = emptyList(),
) : IDataTransferable<UserDto> {

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

    fun passwordValid(password: String): Boolean {
        return hashPassword(password) == this.password
    }

    override fun toTransferable(di: DI): UserDto {
        return UserDto(id, username, groups)
    }

}
