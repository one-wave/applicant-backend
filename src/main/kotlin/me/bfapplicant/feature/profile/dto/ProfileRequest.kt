package me.bfapplicant.feature.profile.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.enums.EnvBothHands
import me.bfapplicant.domain.enums.EnvEyeSight
import me.bfapplicant.domain.enums.EnvHandWork
import me.bfapplicant.domain.enums.EnvLiftPower
import me.bfapplicant.domain.enums.EnvLstnTalk
import me.bfapplicant.domain.enums.EnvStndWalk
import java.time.LocalDate

@Schema(description = "프로필 수정 요청")
data class ProfileRequest(
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
