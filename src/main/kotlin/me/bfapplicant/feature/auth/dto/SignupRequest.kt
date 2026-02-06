package me.bfapplicant.feature.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import me.bfapplicant.domain.enums.EnvBothHands
import me.bfapplicant.domain.enums.EnvEyeSight
import me.bfapplicant.domain.enums.EnvHandWork
import me.bfapplicant.domain.enums.EnvLiftPower
import me.bfapplicant.domain.enums.EnvLstnTalk
import me.bfapplicant.domain.enums.EnvStndWalk
import java.time.LocalDate

@Schema(description = "회원가입 요청")
data class SignupRequest(
    @field:NotBlank
    @Schema(description = "성", example = "홍")
    val lastName: String,

    @field:NotBlank
    @Schema(description = "이름", example = "길동")
    val firstName: String,

    @field:NotBlank @field:Email
    @Schema(description = "이메일", example = "hong@example.com")
    val email: String,

    @field:NotBlank @field:Size(min = 8)
    @Schema(description = "비밀번호 (8자 이상)", example = "password123")
    val password: String,

    @Schema(description = "전화번호", example = "010-1234-5678", nullable = true)
    val userPhone: String? = null,

    @Schema(description = "생년월일", example = "1995-03-15", nullable = true)
    val birthDate: LocalDate? = null,

    @Schema(description = "양손작업 능력", example = "BOTH_HANDS")
    val envBothHands: EnvBothHands,

    @Schema(description = "시력 수준", example = "DAILY_ACTIVITY")
    val envEyeSight: EnvEyeSight,

    @Schema(description = "손작업 정밀도", example = "SMALL_ASSEMBLY")
    val envHandWork: EnvHandWork,

    @Schema(description = "들기 힘", example = "UNDER_5KG")
    val envLiftPower: EnvLiftPower,

    @Schema(description = "듣고 말하기 능력", example = "FLUENT")
    val envLstnTalk: EnvLstnTalk,

    @Schema(description = "서서 걷기 능력", example = "PROLONGED")
    val envStndWalk: EnvStndWalk
)
