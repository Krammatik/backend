package io.github.krammatik.user

import io.ktor.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/user/{id}")
data class UserById(val id: String)
