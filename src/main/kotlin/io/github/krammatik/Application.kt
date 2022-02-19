package io.github.krammatik

import io.github.krammatik.encrypt.EncryptionService
import io.github.krammatik.plugins.configureMonitoring
import io.github.krammatik.plugins.configureRouting
import io.github.krammatik.plugins.configureSerialization
import io.github.krammatik.user.services.UserService
import io.ktor.locations.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bindSingleton
import org.kodein.di.ktor.di
import org.litote.kmongo.KMongo
import kotlin.system.exitProcess

@KtorExperimentalLocationsAPI
fun main() {
    if (System.getenv("ENCRYPT_SECRET").isEmpty()) {
        println("ENCRYPT_SECRET not set!")
        exitProcess(1)
    }
    if (System.getenv("MONGO_CONNECTION_STRING").isEmpty()) {
        println("ENCRYPT_SECRET not set!")
        exitProcess(1)
    }
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        // Dependency injection
        di {
            bindSingleton { EncryptionService(System.getenv("ENCRYPT_SECRET")) }
            bindSingleton { KMongo.createClient(System.getenv("MONGO_CONNECTION_STRING")) }

            bindSingleton { UserService(this.di) }
        }
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
