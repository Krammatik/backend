package io.github.krammatik.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.krammatik.authentication.AuthenticationController
import io.github.krammatik.dto.ErrorResponse
import io.github.krammatik.dynamodb.MappingException
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

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    install(CORS) {
        anyHost()
    }
    install(Locations) {}
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
    }
    routing {
        install(StatusPages) {
            exception<InvalidRequestException> { cause ->
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: ""))
            }
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse(cause.message ?: ""))
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden, ErrorResponse(cause.message ?: ""))
            }
            exception<MappingException> { cause ->
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(cause.message ?: ""))
            }
        }
        controller("/auth") { AuthenticationController(instance()) }
    }
}

class InvalidRequestException(override val message: String? = null) : RuntimeException()
class AuthenticationException(override val message: String? = null) : RuntimeException()
class AuthorizationException(override val message: String? = null) : RuntimeException()
