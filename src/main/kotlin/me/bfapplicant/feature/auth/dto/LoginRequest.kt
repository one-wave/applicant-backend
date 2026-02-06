package me.bfapplicant.feature.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "로그인 요청")
data class LoginRequest(
    @field:NotBlank @field:Email
    @Schema(description = "이메일", example = "hong@example.com")
    val email: String,

    @field:NotBlank
    @Schema(description = "비밀번호", example = "password123")
    val password: String
)
