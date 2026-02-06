package me.bfapplicant.feature.profile.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Schema(description = "프로필 수정 요청")
data class ProfileRequest(
    @Schema(description = "전화번호", example = "010-1234-5678", nullable = true)
    val userPhone: String? = null,

    @Schema(description = "생년월일", example = "1995-03-15", nullable = true)
    val birthDate: LocalDate? = null,

    @field:NotBlank
    @Schema(description = "양손작업 능력", example = "양손작업 가능",
        allowableValues = ["정보없음", "한손작업 가능", "한손보조작업 가능", "양손작업 가능"])
    val envBothHands: String,

    @field:NotBlank
    @Schema(description = "시력 수준", example = "일상적 활동 가능",
        allowableValues = ["정보없음", "비교적 큰 인쇄물을 읽을 수 있음", "일상적 활동 가능", "아주 작은 글씨를 읽을 수 있음"])
    val envEyeSight: String,

    @field:NotBlank
    @Schema(description = "손작업 정밀도", example = "작은 물품 조립가능",
        allowableValues = ["정보없음", "큰 물품 조립가능", "작은 물품 조립가능", "정밀한 작업가능"])
    val envHandWork: String,

    @field:NotBlank
    @Schema(description = "들기 힘", example = "5Kg 이내의 물건을 다룰 수 있음",
        allowableValues = ["정보없음", "5Kg 이내의 물건을 다룰 수 있음", "5~20Kg의 물건을 다룰 수 있음", "20Kg 이상의 물건을 다룰 수 있음"])
    val envLiftPower: String,

    @field:NotBlank
    @Schema(description = "듣고 말하기 능력", example = "듣고 말하기에 어려움 없음",
        allowableValues = ["정보없음", "듣고 말하는 작업 어려움", "간단한 듣고 말하기 가능", "듣고 말하기에 어려움 없음"])
    val envLstnTalk: String,

    @field:NotBlank
    @Schema(description = "서서 걷기 능력", example = "오랫동안 가능",
        allowableValues = ["정보없음", "서거나 걷는 일 어려움", "일부 서서하는 작업 가능", "오랫동안 가능"])
    val envStndWalk: String
)
