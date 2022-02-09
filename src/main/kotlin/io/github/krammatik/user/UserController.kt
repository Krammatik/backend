package io.github.krammatik.user

import io.github.krammatik.plugins.AuthenticationException
import io.github.krammatik.plugins.ResourceNotFoundException
import io.github.krammatik.user.services.IUserDatabase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class UserController(application: Application) : AbstractDIController(application) {

    private val userDatabase: IUserDatabase by di.instance()

    override fun Route.getRoutes() {
        authenticate {
            getUser()
            getGroups()
        }
    }

    private fun Route.getUser() {
        get("/") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationException()
            val id = principal.payload.getClaim("id")?.asString() ?: throw AuthenticationException()
            val user = userDatabase.getUserById(id) ?: throw ResourceNotFoundException()
            call.respond(user)
        }
    }

    private fun Route.getGroups() {
        get("/groups") {
            val principal = call.principal<JWTPrincipal>() ?: throw AuthenticationException()
            val id = principal.payload.getClaim("id")?.asString() ?: throw AuthenticationException()
            val user = userDatabase.getUserById(id) ?: throw ResourceNotFoundException()
            call.respond(user.groups)
        }
    }

}