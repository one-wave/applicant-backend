package me.bfapplicant.feature.jobSearch.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "매칭 점수가 포함된 채용공고 결과")
data class JobMatchResult(
    @Schema(description = "채용공고 정보")
    val post: JobPostResponse,

    @Schema(description = "총 매칭 점수 (0~100)", example = "90")
    val score: Int,

    @Schema(description = "매칭 점수 세부 내역")
    val details: MatchDetails
)

@Schema(
    description = """
    매칭 점수 세부 내역.
    총점 = envScore + educScore + careerScore + preferenceScore (최대 100점).
    신체환경 조건이 미달인 공고는 검색 결과에서 사전에 제외되므로,
    반환된 결과의 envScore는 항상 0보다 큽니다.
"""
)
data class MatchDetails(
    @Schema(
        description = "신체환경 적합도 (0~60). 양손작업·시력·손작업·들기·듣고말하기·서서걷기 6개 항목 × 10점",
        minimum = "0", maximum = "60", example = "60"
    )
    val envScore: Int,

    @Schema(
        description = "학력 조건 부합도 (0 또는 15). 공고 요구 학력을 충족하면 15점",
        minimum = "0", maximum = "15", example = "15"
    )
    val educScore: Int,

    @Schema(
        description = "경력 조건 부합도 (0 또는 15). 공고 요구 경력을 충족하면 15점",
        minimum = "0", maximum = "15", example = "15"
    )
    val careerScore: Int,

    @Schema(
        description = "선호도 일치 점수 (0~10). 지역 일치 4점 + 급여유형 일치 3점 + 고용형태 일치 3점",
        minimum = "0", maximum = "10", example = "4"
    )
    val preferenceScore: Int
) {
    @get:Schema(description = "총 매칭 점수 (envScore + educScore + careerScore + preferenceScore)", minimum = "0", maximum = "100")
    val total get() = envScore + educScore + careerScore + preferenceScore

    companion object {
        const val ENV_MAX = 60
        const val EDUC_MAX = 15
        const val CAREER_MAX = 15
        const val PREFERENCE_MAX = 10
        const val ENV_PER_FIELD = 10
    }
}
