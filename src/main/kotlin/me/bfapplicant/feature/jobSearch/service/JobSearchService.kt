package me.bfapplicant.feature.jobSearch.service

import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.enums.*
import me.bfapplicant.domain.repository.ApplicantUserInfoRepository
import me.bfapplicant.domain.repository.EnvExcludes
import me.bfapplicant.domain.repository.JobPostQueryRepository
import me.bfapplicant.domain.repository.JobPostRepository
import me.bfapplicant.feature.jobSearch.dto.JobMatchResult
import me.bfapplicant.feature.jobSearch.dto.JobPostResponse
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class JobSearchService(
    private val jobPostQueryRepository: JobPostQueryRepository,
    private val jobPostRepository: JobPostRepository,
    private val applicantUserInfoRepository: ApplicantUserInfoRepository,
    private val matchScoreCalculator: MatchScoreCalculator
) {

    fun search(filter: JobSearchFilter, pageable: Pageable): Page<JobPostResponse> {
        val userInfo = resolveUserInfo(filter)

        if (userInfo == null && hasNoFilter(filter)) {
            return jobPostRepository.findAllBy(pageable).map(JobPostResponse::from)
        }

        val envExcludes = buildEnvExcludes(userInfo)
        val educExcludes = buildEducExcludes(userInfo)
        val careerMonths = userInfo?.let { parseCareerMonths(it) }

        if (filter.sortBy == SortType.MATCH_SCORE) {
            return searchWithMatchScore(filter, envExcludes, educExcludes, careerMonths, userInfo, pageable)
        }

        return jobPostQueryRepository
            .search(filter, envExcludes, educExcludes, careerMonths, pageable)
            .map(JobPostResponse::from)
    }

    fun searchWithScore(filter: JobSearchFilter, pageable: Pageable): Page<JobMatchResult> {
        val userInfo = resolveUserInfo(filter)
        val envExcludes = buildEnvExcludes(userInfo)
        val educExcludes = buildEducExcludes(userInfo)
        val careerMonths = userInfo?.let { parseCareerMonths(it) }

        val allMatching = jobPostQueryRepository.search(
            filter.copy(sortBy = SortType.RECENT),
            envExcludes, educExcludes, careerMonths,
            Pageable.unpaged()
        ).content

        val scored = allMatching
            .map { post ->
                val details = matchScoreCalculator.calculate(post, userInfo, filter)
                JobMatchResult(JobPostResponse.from(post), details.total, details)
            }
            .sortedByDescending { it.score }

        val start = pageable.offset.toInt().coerceAtMost(scored.size)
        val end = (start + pageable.pageSize).coerceAtMost(scored.size)
        return PageImpl(scored.subList(start, end), pageable, scored.size.toLong())
    }

    private fun searchWithMatchScore(
        filter: JobSearchFilter,
        envExcludes: EnvExcludes,
        educExcludes: List<String>?,
        careerMonths: Int?,
        userInfo: ApplicantUserInfo?,
        pageable: Pageable
    ): Page<JobPostResponse> {
        val allMatching = jobPostQueryRepository.search(
            filter, envExcludes, educExcludes, careerMonths, Pageable.unpaged()
        ).content

        val sorted = allMatching
            .map { it to matchScoreCalculator.calculate(it, userInfo, filter).total }
            .sortedByDescending { it.second }
            .map { JobPostResponse.from(it.first) }

        val start = pageable.offset.toInt().coerceAtMost(sorted.size)
        val end = (start + pageable.pageSize).coerceAtMost(sorted.size)
        return PageImpl(sorted.subList(start, end), pageable, sorted.size.toLong())
    }

    private fun resolveUserInfo(filter: JobSearchFilter): ApplicantUserInfo? =
        filter.userId?.let { applicantUserInfoRepository.findByUserUserId(it) }

    private fun hasNoFilter(filter: JobSearchFilter): Boolean =
        filter.empTypes == null
            && filter.salaryTypes == null
            && filter.minSalary == null
            && filter.maxSalary == null
            && filter.region == null
            && filter.keyword == null
            && filter.sortBy == SortType.RECENT

    private fun buildEnvExcludes(userInfo: ApplicantUserInfo?): EnvExcludes {
        if (userInfo == null) return EnvExcludes()
        return EnvExcludes(
            bothHands = getExcludedLabels<EnvBothHands>(userInfo.envBothHands),
            eyeSight = getExcludedLabels<EnvEyeSight>(userInfo.envEyeSight),
            handWork = getExcludedLabels<EnvHandWork>(userInfo.envHandWork),
            liftPower = getExcludedLabels<EnvLiftPower>(userInfo.envLiftPower),
            lstnTalk = getExcludedLabels<EnvLstnTalk>(userInfo.envLstnTalk),
            stndWalk = getExcludedLabels<EnvStndWalk>(userInfo.envStndWalk)
        )
    }

    private inline fun <reified T> getExcludedLabels(userValue: String): List<String>?
        where T : Enum<T>, T : EnvCondition {
        val capability = enumValues<T>().find { it.label == userValue } ?: return null
        if (capability.level == 0) return null
        return enumValues<T>()
            .filter { it.level > capability.level }
            .map { it.label }
            .ifEmpty { null }
    }

    private fun buildEducExcludes(userInfo: ApplicantUserInfo?): List<String>? {
        if (userInfo == null) return null
        // TODO: compare with user's education when field is available on ApplicantUserInfo
        return null
    }

    private fun parseCareerMonths(userInfo: ApplicantUserInfo): Int? {
        // TODO: compare with user's career when field is available on ApplicantUserInfo
        return null
    }
}
