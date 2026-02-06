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

    @Schema(
        description = "양손작업 능력 — IMPOSSIBLE: 불가능 / ONE_HAND: 한손작업 가능 / ONE_HAND_ASSIST: 한손보조작업 가능 / BOTH_HANDS: 양손작업 가능",
        example = "BOTH_HANDS"
    )
    val envBothHands: EnvBothHands,

    @Schema(
        description = "시력 수준 — IMPOSSIBLE: 불가능 / LARGE_PRINT: 비교적 큰 인쇄물을 읽을 수 있음 / DAILY_ACTIVITY: 일상적 활동 가능 / FINE_PRINT: 아주 작은 글씨를 읽을 수 있음",
        example = "DAILY_ACTIVITY"
    )
    val envEyeSight: EnvEyeSight,

    @Schema(
        description = "손작업 정밀도 — IMPOSSIBLE: 불가능 / LARGE_ASSEMBLY: 큰 물품 조립가능 / SMALL_ASSEMBLY: 작은 물품 조립가능 / PRECISION: 정밀한 작업가능",
        example = "SMALL_ASSEMBLY"
    )
    val envHandWork: EnvHandWork,

    @Schema(
        description = "들기 힘 — IMPOSSIBLE: 불가능 / UNDER_5KG: 5Kg 이내의 물건을 다룰 수 있음 / UNDER_20KG: 5~20Kg의 물건을 다룰 수 있음 / OVER_20KG: 20Kg 이상의 물건을 다룰 수 있음",
        example = "UNDER_5KG"
    )
    val envLiftPower: EnvLiftPower,

    @Schema(
        description = "듣고 말하기 능력 — IMPOSSIBLE: 불가능 / DIFFICULT: 듣고 말하는 작업 어려움 / SIMPLE: 간단한 듣고 말하기 가능 / FLUENT: 듣고 말하기에 어려움 없음",
        example = "FLUENT"
    )
    val envLstnTalk: EnvLstnTalk,

    @Schema(
        description = "서서 걷기 능력 — IMPOSSIBLE: 불가능 / DIFFICULT: 서거나 걷는 일 어려움 / PARTIAL: 일부 서서하는 작업 가능 / PROLONGED: 오랫동안 가능",
        example = "PROLONGED"
    )
    val envStndWalk: EnvStndWalk
)
