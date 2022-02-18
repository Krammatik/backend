@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.github.krammatik.user

import io.ktor.locations.*

@Location("/user")
data class Users(val page: Int, val count: Int)

@Location("/user/{id}")
data class UserById(val id: String)
