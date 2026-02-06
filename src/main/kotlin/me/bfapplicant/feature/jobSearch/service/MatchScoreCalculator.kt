package me.bfapplicant.feature.jobSearch.service

import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.JobPost
import me.bfapplicant.domain.enums.*
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import me.bfapplicant.feature.jobSearch.dto.MatchDetails
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Component
class MatchScoreCalculator {

    // Batch scoring — required for relative salary percentile
    fun calculateBatch(
        posts: List<JobPost>,
        userInfo: ApplicantUserInfo?,
        resume: ApplicantUserResume?,
        filter: JobSearchFilter
    ): List<Pair<JobPost, MatchDetails>> {
        val salaryPercentiles = computeSalaryPercentiles(posts)
        val resumeReflected = hasResumeData(resume)
        val today = LocalDate.now()

        return posts.map { post ->
            post to MatchDetails(
                envFit = envFit(post, userInfo),
                salaryScore = salaryScore(salaryPercentiles[post.jobPostId] ?: 0.5),
                stabilityScore = stabilityScore(post.empType),
                qualificationFit = qualificationFit(post, resume, resumeReflected),
                preferenceScore = preferenceScore(post, filter),
                freshnessScore = freshnessScore(post.regDt, today),
                resumeReflected = resumeReflected
            )
        }
    }

    // --- env fit (max 18) ---

    private fun envFit(post: JobPost, userInfo: ApplicantUserInfo?): Int {
        if (userInfo == null) return MatchDetails.ENV_MAX
        return envField(userInfo.envBothHands, EnvBothHands.fromLabel(post.envBothHands)) +
            envField(userInfo.envEyeSight, EnvEyeSight.fromLabel(post.envEyeSight)) +
            envField(userInfo.envHandWork, EnvHandWork.fromLabel(post.envHandWork)) +
            envField(userInfo.envLiftPower, EnvLiftPower.fromLabel(post.envLiftPower)) +
            envField(userInfo.envLstnTalk, EnvLstnTalk.fromLabel(post.envLstnTalk)) +
            envField(userInfo.envStndWalk, EnvStndWalk.fromLabel(post.envStndWalk))
    }

    private fun envField(userCap: EnvCondition, jobReq: EnvCondition): Int = when {
        jobReq.level == 0 -> MatchDetails.ENV_PER_FIELD
        userCap.level == 0 -> 0
        userCap.level >= jobReq.level -> MatchDetails.ENV_PER_FIELD
        else -> 0
    }

    // --- salary score (max 25, percentile-based) ---

    private fun computeSalaryPercentiles(posts: List<JobPost>): Map<java.util.UUID, Double> {
        if (posts.isEmpty()) return emptyMap()

        val normalized = posts.map { it.jobPostId!! to normalizeToMonthly(it.salary, it.salaryType) }
        if (posts.size == 1) return mapOf(normalized[0].first to 1.0)

        val grouped = normalized.groupBy({ it.second }, { it.first })
            .toSortedMap()

        val n = normalized.size
        val result = mutableMapOf<java.util.UUID, Double>()
        var cumulative = 0

        for ((_, ids) in grouped) {
            val avgRank = cumulative + (ids.size - 1) / 2.0
            val percentile = avgRank / (n - 1)
            ids.forEach { result[it] = percentile }
            cumulative += ids.size
        }
        return result
    }

    private fun normalizeToMonthly(salary: BigDecimal, salaryType: String): BigDecimal = when (salaryType) {
        "시급" -> salary.multiply(BigDecimal(209))
        "일급" -> salary.multiply(BigDecimal(22))
        "연봉" -> salary.divide(BigDecimal(12), 0, RoundingMode.HALF_UP)
        else -> salary // 월급 or unknown
    }

    private fun salaryScore(percentile: Double): Int =
        (percentile * MatchDetails.SALARY_MAX).toInt().coerceIn(0, MatchDetails.SALARY_MAX)

    // --- stability score (max 12) ---

    private val STABILITY = mapOf("상용직" to 12, "계약직" to 7, "시간제" to 3)

    private fun stabilityScore(empType: String): Int = STABILITY[empType] ?: 5

    // --- qualification fit (max 15 = educ 8 + career 7) ---

    private fun qualificationFit(post: JobPost, resume: ApplicantUserResume?, reflected: Boolean): Int =
        educScore(post, resume, reflected) + careerScore(post, resume, reflected)

    private fun educScore(post: JobPost, resume: ApplicantUserResume?, reflected: Boolean): Int {
        val reqLevel = ReqEduc.fromLabel(post.reqEduc).level
        if (reqLevel == 0) return MatchDetails.EDUC_MAX
        if (!reflected) return 6 // moderate default

        val userLevel = resume!!.educations.maxOfOrNull { it.degree.level } ?: 0
        val gap = userLevel - reqLevel
        return when {
            gap >= 2 -> 8
            gap == 1 -> 6
            gap == 0 -> 5
            else -> 0
        }
    }

    private fun careerScore(post: JobPost, resume: ApplicantUserResume?, reflected: Boolean): Int {
        val reqMonths = CareerParser.toMonths(post.reqCareer)
        if (reqMonths == 0) return MatchDetails.CAREER_MAX
        if (!reflected) return 5 // moderate default

        val userMonths = resume!!.careers.sumOf { it.toMonths() }
        if (userMonths < reqMonths) return 0

        val ratio = userMonths.toDouble() / reqMonths
        return when {
            ratio >= 2.0 -> 7
            ratio >= 1.5 -> 6
            ratio >= 1.0 -> 5
            else -> 4
        }
    }

    // --- preference score (max 15) ---

    private fun preferenceScore(post: JobPost, filter: JobSearchFilter): Int {
        var score = 0
        if (filter.region != null) {
            val variants = Region.normalize(filter.region)
            if (variants.any { post.jobLocation.startsWith(it) }) score += 6
        }
        if (filter.salaryTypes != null && post.salaryType in filter.salaryTypes) score += 4
        if (filter.empTypes != null && post.empType in filter.empTypes) score += 5
        return score
    }

    // --- freshness score (max 15) ---

    private val DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd")

    private fun freshnessScore(regDt: Long, today: LocalDate): Int {
        val regDate = LocalDate.parse(regDt.toString(), DATE_FMT)
        val days = ChronoUnit.DAYS.between(regDate, today)
        return when {
            days < 3  -> 15
            days < 7  -> 12
            days < 14 -> 9
            days < 30 -> 6
            days < 60 -> 3
            else      -> 1
        }
    }

    private fun hasResumeData(resume: ApplicantUserResume?): Boolean =
        resume != null && (resume.educations.isNotEmpty() || resume.careers.isNotEmpty())
}
