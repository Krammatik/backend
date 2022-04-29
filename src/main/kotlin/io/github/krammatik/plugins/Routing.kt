package io.github.krammatik.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.krammatik.authentication.AuthenticationController
import io.github.krammatik.course.CourseController
import io.github.krammatik.dto.ErrorResponseDto
import io.github.krammatik.groups.GroupController
import io.github.krammatik.task.TaskController
import io.github.krammatik.user.UserController
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.controller

fun Application.configureRouting() {
    install(Locations) {}
    install(DefaultHeaders) {
        header("Access-Control-Allow-Origin", "*")
    }
    authentication {
        jwt {
            verifier(
                JWT.require(Algorithm.HMAC256(System.getenv("ENCRYPT_SECRET")))
                    .withIssuer("https://krammatik.deathsgun.xyz/").build()
            )
            validate {
                if (it.payload.getClaim("id").asString() != "") {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
        }
        jwt("admin") {
            verifier(
                JWT.require(Algorithm.HMAC256(System.getenv("ENCRYPT_SECRET")))
                    .withIssuer("https://krammatik.deathsgun.xyz/").build()
            )
            validate {
                if (it.payload.getClaim("id").asString() != "" && it.payload.getClaim("groups")
                        .asList(String::class.java).contains("admin")
                ) {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
        }
    }
    routing {
        install(StatusPages) {
            exception<InvalidRequestException> { cause ->
                call.respond(HttpStatusCode.BadRequest, ErrorResponseDto(cause.message ?: ""))
            }
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponseDto(cause.message ?: ""))
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden, ErrorResponseDto(cause.message ?: ""))
            }
            exception<NotFoundException> { cause ->
                call.respond(HttpStatusCode.NotFound, ErrorResponseDto(cause.message ?: ""))
            }
            exception<ForbiddenException> { cause ->
                call.respond(HttpStatusCode.Forbidden, ErrorResponseDto(cause.message ?: ""))
            }
            exception<ResourceAlreadyExistsException> { cause ->
                call.respond(HttpStatusCode.Forbidden, ErrorResponseDto(cause.message ?: ""))
            }
            exception<NotFoundException> { cause ->
                call.respond(HttpStatusCode.NotFound, ErrorResponseDto(cause.message ?: ""))
            }
        }

        controller("/auth") { AuthenticationController(instance()) }
        controller("/users") { UserController(instance()) }
        controller("/course") { CourseController(instance()) }
        controller("/task") { TaskController(instance()) }
        controller("/group") { GroupController(instance()) }
    }
}

class InvalidRequestException(override val message: String? = null) : RuntimeException()
class AuthenticationException(override val message: String? = null) : RuntimeException()
class AuthorizationException(override val message: String? = null) : RuntimeException()
class NotFoundException(override val message: String? = null) : RuntimeException()
class ResourceAlreadyExistsException(override val message: String? = null) : RuntimeException()
class ForbiddenException(override val message: String? = null) : RuntimeException()