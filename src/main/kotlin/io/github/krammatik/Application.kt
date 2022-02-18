package io.github.krammatik

import io.github.krammatik.encrypt.EncryptionService
import io.github.krammatik.plugins.configureMonitoring
import io.github.krammatik.plugins.configureRouting
import io.github.krammatik.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.slf4j.LoggerFactory

val environment = applicationEngineEnvironment {
    log = LoggerFactory.getLogger("Krammatik")
    module(Application::module)
}

fun main() {
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    // Dependency injection
    di {
        bind { singleton { EncryptionService(System.getenv("ENCRYPT_SECRET")) } }
    }
    configureRouting()
    configureMonitoring()
    configureSerialization()
}