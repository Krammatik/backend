package io.github.krammatik.groups.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.models.Group
import io.github.krammatik.user.dto.UserDto
import org.kodein.di.DI

data class GroupDto(
    val id: String,
    val creator: String,
    val name: String,
    val users: List<UserDto>
) : IDataTransferable<Group> {

    override fun toTransferable(di: DI): Group {
        return Group(this.id, this.creator, this.name, users.map { it.toTransferable(di) } )
    }


}