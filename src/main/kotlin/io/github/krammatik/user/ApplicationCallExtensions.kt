package io.github.krammatik.user

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun ApplicationCall.userId(): String {
    val principal = principal<JWTPrincipal>()!!
    return principal.payload.getClaim("id").asString()
}

fun ApplicationCall.groups(): List<String> {
    val principal = principal<JWTPrincipal>()!!
    return principal.payload.getClaim("groups").asList(String::class.java)
}

fun ApplicationCall.allowed(vararg groups: String): Boolean {
    return groups().any { groups.contains(it) } || groups().contains("admin")
}