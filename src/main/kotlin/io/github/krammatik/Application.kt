package io.github.krammatik

import io.github.krammatik.encrypt.EncryptionService
import io.github.krammatik.plugins.configureMonitoring
import io.github.krammatik.plugins.configureRouting
import io.github.krammatik.plugins.configureSerialization
import io.github.krammatik.user.IUserDatabase
import io.github.krammatik.user.UserDynamoDatabase
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        // Dependency injection
        di {
            bind { singleton { EncryptionService(System.getenv("ENCRYPT_SECRET")) } }
            bind<IUserDatabase> { singleton { UserDynamoDatabase() } }
        }
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
