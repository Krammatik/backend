package io.github.krammatik.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.krammatik.authentication.AuthenticationController
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
            exception<InvalidRequestException> {
                call.respond(HttpStatusCode.BadRequest)
            }
            exception<AuthenticationException> {
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> {
                call.respond(HttpStatusCode.Forbidden)
            }
            exception<MappingException> {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        controller("/auth") { AuthenticationController(instance()) }
    }
}

class InvalidRequestException : RuntimeException()
class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
