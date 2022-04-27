package io.github.krammatik.groups

import io.github.krammatik.course.dto.CourseCreationRequest
import io.github.krammatik.groups.dto.GroupCreationRequest
import io.github.krammatik.groups.services.IGroupDatabase
import io.github.krammatik.plugins.ForbiddenException
import io.github.krammatik.plugins.InvalidRequestException
import io.github.krammatik.plugins.NotFoundException
import io.github.krammatik.user.allowed
import io.github.krammatik.user.services.IUserDatabase
import io.github.krammatik.user.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class GroupController(application: Application) : AbstractDIController(application) {

    val userDatabase : IUserDatabase by di.instance()
    val groupDatabase : IGroupDatabase by di.instance()

    private fun Route.getGroups() = get {
        val groups = groupDatabase.getGroups()
        call.respond(HttpStatusCode.OK, groups)
    }

    private fun Route.createGroup() = post {
        if (!call.allowed("creator")) {
            throw ForbiddenException()
        }
        val request = call.receive<GroupCreationRequest>()
        request.creatorUserId = call.userId()
        val course = groupDatabase.createGroup(request)
        call.respond(HttpStatusCode.Created, course)
    }

    private fun Route.getCurrentUserGroups() = get("/user") {
        val principal = call.principal<JWTPrincipal>()!!
        val userId = principal.payload.getClaim("id").asString()
        val user = userDatabase.getUserById(userId) ?: throw NotFoundException("User id '${userId}' not found")
        val groups = groupDatabase.getGroupsByUser(user);
        call.respond(HttpStatusCode.OK, groups);
    }

    private fun Route.getGroupsById() = get("/{id}") {
        val id = call.parameters["id"] ?: throw InvalidRequestException("No group id provided")
        val foundGroup = groupDatabase.getGroupById(id) ?: throw NotFoundException("Group id '$id' not found")
        call.respond(HttpStatusCode.OK, foundGroup);
    }

    override fun Route.getRoutes() {
        authenticate {
            getGroupsById()
            getGroups()
            createGroup()
            getCurrentUserGroups()
        }
    }

}