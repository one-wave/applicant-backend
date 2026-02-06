package me.bfapplicant.feature.profile.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.ApplicantUser
import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.enums.EnvBothHands
import me.bfapplicant.domain.enums.EnvEyeSight
import me.bfapplicant.domain.enums.EnvHandWork
import me.bfapplicant.domain.enums.EnvLiftPower
import me.bfapplicant.domain.enums.EnvLstnTalk
import me.bfapplicant.domain.enums.EnvStndWalk
import java.time.LocalDate

@Schema(description = "프로필 조회 응답")
data class ProfileResponse(
    @Schema(description = "성", example = "홍")
    val lastName: String,

    @Schema(description = "이름", example = "길동")
    val firstName: String,

    @Schema(description = "이메일", example = "hong@example.com")
    val email: String,

    @Schema(description = "전화번호", example = "010-1234-5678", nullable = true)
    val userPhone: String?,

    @Schema(description = "생년월일", example = "1995-03-15", nullable = true)
    val birthDate: LocalDate?,

    @Schema(description = "양손작업 능력", example = "BOTH_HANDS")
    val envBothHands: EnvBothHands,
    @Schema(description = "양손작업 능력 표시명", example = "양손작업 가능")
    val envBothHandsLabel: String,

    @Schema(description = "시력 수준", example = "DAILY_ACTIVITY")
    val envEyeSight: EnvEyeSight,
    @Schema(description = "시력 수준 표시명", example = "일상적 활동 가능")
    val envEyeSightLabel: String,

    @Schema(description = "손작업 정밀도", example = "SMALL_ASSEMBLY")
    val envHandWork: EnvHandWork,
    @Schema(description = "손작업 정밀도 표시명", example = "작은 물품 조립가능")
    val envHandWorkLabel: String,

    @Schema(description = "들기 힘", example = "UNDER_5KG")
    val envLiftPower: EnvLiftPower,
    @Schema(description = "들기 힘 표시명", example = "5Kg 이내의 물건을 다룰 수 있음")
    val envLiftPowerLabel: String,

    @Schema(description = "듣고 말하기 능력", example = "FLUENT")
    val envLstnTalk: EnvLstnTalk,
    @Schema(description = "듣고 말하기 능력 표시명", example = "듣고 말하기에 어려움 없음")
    val envLstnTalkLabel: String,

    @Schema(description = "서서 걷기 능력", example = "PROLONGED")
    val envStndWalk: EnvStndWalk,
    @Schema(description = "서서 걷기 능력 표시명", example = "오랫동안 가능")
    val envStndWalkLabel: String
) {
    companion object {
        fun from(user: ApplicantUser, info: ApplicantUserInfo) = ProfileResponse(
            lastName = user.lastName,
            firstName = user.firstName,
            email = user.userEmailContact,
            userPhone = info.userPhone,
            birthDate = info.birthDate,
            envBothHands = info.envBothHands,
            envBothHandsLabel = info.envBothHands.label,
            envEyeSight = info.envEyeSight,
            envEyeSightLabel = info.envEyeSight.label,
            envHandWork = info.envHandWork,
            envHandWorkLabel = info.envHandWork.label,
            envLiftPower = info.envLiftPower,
            envLiftPowerLabel = info.envLiftPower.label,
            envLstnTalk = info.envLstnTalk,
            envLstnTalkLabel = info.envLstnTalk.label,
            envStndWalk = info.envStndWalk,
            envStndWalkLabel = info.envStndWalk.label
        )
    }
}
