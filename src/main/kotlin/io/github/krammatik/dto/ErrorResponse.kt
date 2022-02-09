package io.github.krammatik.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)
