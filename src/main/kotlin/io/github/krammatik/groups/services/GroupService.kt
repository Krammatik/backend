package io.github.krammatik.groups.services

import com.mongodb.client.MongoClient
import io.github.krammatik.groups.dto.GroupCreationRequest
import io.github.krammatik.models.Course
import io.github.krammatik.models.Group
import io.github.krammatik.models.User
import org.kodein.di.DI
import org.kodein.di.instance
import org.litote.kmongo.*

class GroupService(val di: DI) : IGroupDatabase {

    private val mongoClient: MongoClient by di.instance()

    private val database get() = this.mongoClient.getDatabase("krammatik")
    private val groupCollection get() = this.database.getCollection<Group>("groups")

    override fun getGroups(): List<Group> {
        return this.groupCollection.find().toList()
    }

    override fun getGroupByName(name: String): Group? {
        return this.groupCollection.find(Group::name eq name).first()
    }

    override fun getGroupById(id: String): Group? {
        return this.groupCollection.find(Group::id eq id).first()
    }

    override fun getGroupsByUser(user: User): List<Group> {
        return this.groupCollection.find(Group::users / Group::id `in` user.id).toList()
    }

    override fun createGroup(request: GroupCreationRequest) {
        this.getGroupByName(request.groupName) ?: return
        this.groupCollection.insertOne(request.toTransferable(di))
    }

}