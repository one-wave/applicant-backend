package me.bfapplicant.feature.profile.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "비밀번호 변경 요청")
data class ChangePasswordRequest(
    @field:NotBlank
    @Schema(description = "현재 비밀번호")
    val currentPassword: String,

    @field:NotBlank @field:Size(min = 8)
    @Schema(description = "새 비밀번호 (8자 이상)")
    val newPassword: String
)
