package me.bfapplicant.feature.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토큰 응답")
data class TokenResponse(
    @Schema(description = "Access Token (Bearer)")
    val accessToken: String,

    @Schema(description = "Refresh Token")
    val refreshToken: String
)
