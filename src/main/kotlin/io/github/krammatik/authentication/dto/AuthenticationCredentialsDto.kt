package io.github.krammatik.authentication.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationCredentialsDto(
    val username: String,
    val password: String
)
