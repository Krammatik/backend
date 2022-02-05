package io.github.krammatik.authentication

import io.github.krammatik.authentication.dto.AuthenticationCredentials
import io.github.krammatik.encrypt.EncryptionService
import io.github.krammatik.plugins.AuthenticationException
import io.github.krammatik.user.Account
import io.github.krammatik.user.IUserDatabase
import io.github.krammatik.user.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class AuthenticationController(application: Application) : AbstractDIController(application) {

    private val userDatabase: IUserDatabase by di.instance()
    private val encryptionService: EncryptionService by di.instance()

    override fun Route.getRoutes() {
        post("/login") {
            val credentials = call.receive<AuthenticationCredentials>()
            if (credentials.username.isEmpty() || credentials.password.isEmpty()) {
                throw AuthenticationException()
            }
            val user = userDatabase.getAccountByName(credentials.username) ?: throw AuthenticationException()
            if (!user.passwordValid(credentials.password)) {
                throw AuthenticationException()
            }
            val time = LocalDateTime.now().plusHours(8).toEpochSecond(ZoneOffset.UTC)
            val cookieValue = encryptionService.encrypt("${user.id}||$time")
            call.response.cookies.append(Cookie("AuthSessionId", cookieValue))
            call.respond(HttpStatusCode.NoContent)
        }
        put("/register") {
            val credentials = call.receive<AuthenticationCredentials>()
            val password = Account.hashPassword(credentials.password)
            val user = userDatabase.createUser(
                User(UUID.randomUUID().toString(), credentials.username, listOf("user")),
                password
            )
            call.respond(HttpStatusCode.Created, user)
        }
        get("/validate") {
            val token = call.request.header("Authorization") ?: throw AuthenticationException()
            val decrypted = encryptionService.decrypt(token).split("||")
            if (decrypted.size != 2) {
                throw AuthenticationException()
            }
            val time = LocalDateTime.ofInstant(Instant.ofEpochSecond(decrypted[1].toLong()), ZoneId.of("Z"))
            if (time.isAfter(LocalDateTime.now())) {
                throw AuthenticationException()
            }
            if (userDatabase.getUserById(decrypted[0]) != null) {
                throw AuthenticationException()
            }
            call.respond(HttpStatusCode.NoContent)
        }
        get("/logout") {
            call.response.cookies.appendExpired("AuthSessionId")
            call.respond(HttpStatusCode.NoContent)
        }
    }

}