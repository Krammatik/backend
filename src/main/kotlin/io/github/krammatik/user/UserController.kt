package io.github.krammatik.user

import io.github.krammatik.plugins.InvalidRequestException
import io.github.krammatik.plugins.NotFoundException
import io.github.krammatik.user.services.IUserDatabase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class UserController(application: Application) : AbstractDIController(application) {

    private val userDatabase: IUserDatabase by di.instance()

    private fun Route.getUsers() = get {
        val users = userDatabase.getUsers(call.parameters["page"]?.toInt() ?: 0, call.parameters["count"]?.toInt() ?: 100)
        call.respond(HttpStatusCode.OK, users)
    }

    private fun Route.getCurrentUser() = get("/user") {
        val principal = call.principal<JWTPrincipal>()!!
        val userId = principal.payload.getClaim("id").asString()
        val user = userDatabase.getUserById(userId) ?: throw NotFoundException("User id '${userId}' not found")
        call.respond(HttpStatusCode.OK, user)
    }

    private fun Route.getUserById() = get("/{id}") {
        val userId = call.parameters["id"] ?: throw InvalidRequestException("No user id provided")
        val user = userDatabase.getUserById(userId) ?: throw NotFoundException("User id '${userId}' not found")
        call.respond(HttpStatusCode.OK, user)
    }

    override fun Route.getRoutes() {
        authenticate {
            getUsers()
            getCurrentUser()
            getUserById()
        }
    }
}