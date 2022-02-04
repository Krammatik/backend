package io.github.krammatik

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.github.krammatik.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
