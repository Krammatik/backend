package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.groups.dto.GroupDto
import io.github.krammatik.user.services.UserService
import org.bson.codecs.pojo.annotations.BsonId
import org.kodein.di.DI
import org.kodein.di.instance
import java.util.*

data class Group(
    @BsonId
    var id : String = UUID.randomUUID().toString(),
    var creator: String,
    var name : String,
    val users: List<User> = emptyList()
) : IDataTransferable<GroupDto> {

    override fun toTransferable(di: DI): GroupDto {
        return GroupDto(this.id, this.creator, this.name, this.users.map { it.toTransferable(di) } )
    }

}