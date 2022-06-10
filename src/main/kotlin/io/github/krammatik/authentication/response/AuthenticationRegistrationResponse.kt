package io.github.krammatik.authentication.response

import io.github.krammatik.user.dto.UserDto

@kotlinx.serialization.Serializable
data class AuthenticationRegistrationResponse(val user: UserDto, val token : String)