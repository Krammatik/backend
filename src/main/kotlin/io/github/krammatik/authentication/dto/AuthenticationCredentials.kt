package io.github.krammatik.authentication.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationCredentials(
    val username: String,
    val password: String
)
