package io.github.krammatik.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.krammatik.authentication.dto.AuthenticationCredentialsDto
import io.github.krammatik.authentication.response.AuthenticationRegistrationResponse
import io.github.krammatik.models.User
import io.github.krammatik.plugins.AuthenticationException
import io.github.krammatik.plugins.InvalidRequestException
import io.github.krammatik.user.dto.UserDto
import io.github.krammatik.user.services.IUserDatabase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.util.*
import java.util.concurrent.TimeUnit

class AuthenticationController(application: Application) : AbstractDIController(application) {

    private val userDatabase: IUserDatabase by di.instance()

    private fun createJWTToken(user : User) = JWT.create()
        .withClaim("id", user.id)
        .withClaim("groups", user.groups)
        .withIssuer("https://krammatik.deathsgun.xyz/")
        .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8)))
        .sign(Algorithm.HMAC256(System.getenv("ENCRYPT_SECRET")))

    private fun Route.login() = post("/login") {
        val credentials = call.receive<AuthenticationCredentialsDto>()
        if (credentials.username.isEmpty() || credentials.password.isEmpty()) {
            throw InvalidRequestException("The username and password mustn't be empty.")
        }
        val user = userDatabase.getAccountByName(credentials.username) ?: throw AuthenticationException()
        if (!user.passwordValid(credentials.password)) {
            throw AuthenticationException()
        }
        call.respond(
            hashMapOf(
                "token" to createJWTToken(user)
            )
        )
    }

    private fun Route.register() = post("/register") {
        val credentials = call.receive<AuthenticationCredentialsDto>()
        val password = User.hashPassword(credentials.password)
        val userDTO = UserDto(UUID.randomUUID().toString(), credentials.username, listOf("user"));
        val user = userDatabase.createUser(userDTO, password)
        call.respond(HttpStatusCode.Created, AuthenticationRegistrationResponse(userDTO, createJWTToken(user)));
    }

    private fun Route.validate() = get("/validate") {
        call.respond(HttpStatusCode.NoContent)
    }

    override fun Route.getRoutes() {
        login()
        register()
        authenticate {
            validate()
        }
    }

}