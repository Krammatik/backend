package io.github.krammatik.user

import io.github.krammatik.user.services.IUserDatabase
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

@OptIn(KtorExperimentalLocationsAPI::class)
class UserController(application: Application) : AbstractDIController(application) {

    private val userDatabase: IUserDatabase by di.instance()

    private fun Route.getUsers() = get<Users> { param ->
        val users = userDatabase.getUsers(param.page, param.count)
        call.respond(HttpStatusCode.OK, users)
    }

    private fun Route.getUserById() = get<UserById> { param ->
        val user = userDatabase.getUserById(param.id)
            ?: throw io.github.krammatik.plugins.NotFoundException("User id '$param.id' not found")
        call.respond(HttpStatusCode.OK, user)
    }

    override fun Route.getRoutes() {
        getUsers()
        getUserById()
    }
}