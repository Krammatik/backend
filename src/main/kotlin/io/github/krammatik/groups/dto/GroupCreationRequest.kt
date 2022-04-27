package io.github.krammatik.groups.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.models.Group
import io.github.krammatik.models.User
import org.kodein.di.DI

@kotlinx.serialization.Serializable
data class GroupCreationRequest(
    val groupName : String
) : IDataTransferable<Group> {

    var creatorUserId : String = ""

    override fun toTransferable(di: DI): Group {
        return Group(creator = creatorUserId, name = groupName);
    }

}