package me.bfapplicant.feature.profile.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.ApplicantUser
import me.bfapplicant.domain.entity.ApplicantUserInfo
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

    @Schema(description = "양손작업 능력", example = "양손작업 가능")
    val envBothHands: String,

    @Schema(description = "시력 수준", example = "일상적 활동 가능")
    val envEyeSight: String,

    @Schema(description = "손작업 정밀도", example = "작은 물품 조립가능")
    val envHandWork: String,

    @Schema(description = "들기 힘", example = "5Kg 이내의 물건을 다룰 수 있음")
    val envLiftPower: String,

    @Schema(description = "듣고 말하기 능력", example = "듣고 말하기에 어려움 없음")
    val envLstnTalk: String,

    @Schema(description = "서서 걷기 능력", example = "오랫동안 가능")
    val envStndWalk: String
) {
    companion object {
        fun from(user: ApplicantUser, info: ApplicantUserInfo) = ProfileResponse(
            lastName = user.lastName,
            firstName = user.firstName,
            email = user.userEmailContact,
            userPhone = info.userPhone,
            birthDate = info.birthDate,
            envBothHands = info.envBothHands,
            envEyeSight = info.envEyeSight,
            envHandWork = info.envHandWork,
            envLiftPower = info.envLiftPower,
            envLstnTalk = info.envLstnTalk,
            envStndWalk = info.envStndWalk
        )
    }
}
