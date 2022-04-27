package io.github.krammatik.groups.services

import io.github.krammatik.groups.dto.GroupCreationRequest
import io.github.krammatik.models.Group
import io.github.krammatik.models.User

interface IGroupDatabase {

    fun getGroups() : List<Group>

    fun getGroupByName(name: String) : Group?

    fun getGroupById(id: String) : Group?

    fun getGroupsByUser(user: User) : List<Group>

    fun createGroup(request: GroupCreationRequest)

}