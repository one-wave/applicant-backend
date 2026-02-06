package me.bfapplicant.feature.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Schema(description = "공고 지원 요청")
data class ApplyRequest(
    @field:NotNull
    @Schema(description = "지원할 공고 ID")
    val jobPostId: UUID,

    @field:NotNull
    @Schema(description = "제출할 이력서 ID")
    val resumeId: UUID
)
