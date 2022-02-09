package io.github.krammatik

import io.github.krammatik.encrypt.EncryptionService
import io.github.krammatik.plugins.configureMonitoring
import io.github.krammatik.plugins.configureRouting
import io.github.krammatik.plugins.configureSerialization
import io.github.krammatik.user.services.IUserDatabase
import io.github.krammatik.user.services.UserDynamoDatabase
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore


val environment = applicationEngineEnvironment {
    log = LoggerFactory.getLogger("Krammatik")
    connector {
        port = 8080
    }
    if (System.getenv().containsKey("DOCKER")) {
        val keyStoreFile = File("/certs/keystore.jks")
        val keystore = KeyStore.getInstance("JKS")
        keystore.load(FileInputStream(keyStoreFile), "krammatik".toCharArray())
        sslConnector(
            keyStore = keystore,
            keyAlias = "krammatik",
            keyStorePassword = { "krammatik".toCharArray() },
            privateKeyPassword = { "krammatik".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
    }
    module(Application::module)
}

fun main() {
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    // Dependency injection
    di {
        bind { singleton { EncryptionService(System.getenv("ENCRYPT_SECRET")) } }
        bind<IUserDatabase> { singleton { UserDynamoDatabase() } }
    }
    configureRouting()
    configureMonitoring()
    configureSerialization()
}