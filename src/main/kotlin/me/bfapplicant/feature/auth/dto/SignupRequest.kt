package me.bfapplicant.feature.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "회원가입 요청")
data class SignupRequest(
    @field:NotBlank
    @Schema(description = "사용자 이름", example = "홍길동")
    val userName: String,

    @field:NotBlank @field:Email
    @Schema(description = "이메일", example = "hong@example.com")
    val email: String,

    @field:NotBlank @field:Size(min = 8)
    @Schema(description = "비밀번호 (8자 이상)", example = "password123")
    val password: String
)
