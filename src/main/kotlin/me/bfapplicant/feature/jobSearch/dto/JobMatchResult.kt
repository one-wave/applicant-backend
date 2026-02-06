package me.bfapplicant.feature.jobSearch.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "매칭 점수가 포함된 채용공고 결과")
data class JobMatchResult(
    @Schema(description = "채용공고 정보")
    val post: JobPostResponse,

    @Schema(description = "총 매칭 점수 (0~100)", example = "72")
    val score: Int,

    @Schema(description = "매칭 점수 세부 내역")
    val details: MatchDetails
)

@Schema(description = "매칭 점수 세부 내역. 총점 = 6개 항목 합산 (최대 100점)")
data class MatchDetails(
    @Schema(description = "신체환경 적합도 (0~18). 6개 항목 × 3점", minimum = "0", maximum = "18", example = "18")
    val envFit: Int,

    @Schema(description = "급여 매력도 (0~25). 검색 결과 내 상대 순위 기반", minimum = "0", maximum = "25", example = "18")
    val salaryScore: Int,

    @Schema(description = "고용 안정성 (0~12). 상용직 12 / 계약직 7 / 시간제 3", minimum = "0", maximum = "12", example = "12")
    val stabilityScore: Int,

    @Schema(description = "학력·경력 적합도 (0~15). 학력 8 + 경력 7, 초과 충족 시 가점", minimum = "0", maximum = "15", example = "13")
    val qualificationFit: Int,

    @Schema(description = "선호도 일치 (0~15). 지역 6 + 급여유형 4 + 고용형태 5", minimum = "0", maximum = "15", example = "6")
    val preferenceScore: Int,

    @Schema(description = "공고 신선도 (0~15). 최근 등록 공고에 가점", minimum = "0", maximum = "15", example = "12")
    val freshnessScore: Int,

    @Schema(description = "대표이력서 반영 여부. false이면 학력·경력은 기본값으로 산정됩니다.", example = "true")
    val resumeReflected: Boolean
) {
    @get:Schema(description = "총 매칭 점수", minimum = "0", maximum = "100")
    val total get() = envFit + salaryScore + stabilityScore + qualificationFit + preferenceScore + freshnessScore

    companion object {
        const val ENV_MAX = 18
        const val ENV_PER_FIELD = 3
        const val SALARY_MAX = 25
        const val STABILITY_MAX = 12
        const val QUALIFICATION_MAX = 15
        const val EDUC_MAX = 8
        const val CAREER_MAX = 7
        const val PREFERENCE_MAX = 15
        const val FRESHNESS_MAX = 15
    }
}
