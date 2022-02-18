package io.github.krammatik.user

import io.ktor.locations.*

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/user")
data class Users(val page: Int, val count: Int)
