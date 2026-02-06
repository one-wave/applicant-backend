package me.bfapplicant.feature.jobSearch.dto

data class JobMatchResult(
    val post: JobPostResponse,
    val score: Int,
    val details: MatchDetails
)

data class MatchDetails(
    val envScore: Int,
    val educScore: Int,
    val careerScore: Int,
    val preferenceScore: Int
) {
    val total get() = envScore + educScore + careerScore + preferenceScore

    companion object {
        const val ENV_MAX = 60
        const val EDUC_MAX = 15
        const val CAREER_MAX = 15
        const val PREFERENCE_MAX = 10
        const val ENV_PER_FIELD = 10
    }
}
