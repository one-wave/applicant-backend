package me.bfapplicant.feature.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 갱신 요청")
data class RefreshRequest(
    @field:NotBlank
    @Schema(description = "Refresh Token")
    val refreshToken: String
)
