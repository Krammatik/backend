package io.github.krammatik.plugins

import io.github.krammatik.authentication.AuthenticationController
import io.github.krammatik.dynamodb.MappingException
import io.ktor.application.*
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
    install(Locations) {
    }

    routing {
        install(StatusPages) {
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

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
