package io.github.krammatik.user.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.models.User
import kotlinx.serialization.Serializable
import org.kodein.di.DI

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val groups: List<String>
) : IDataTransferable<User> {

    override fun toTransferable(di: DI): User {
        return User(id, username, "", groups)
    }
}
