package me.bfapplicant.feature.jobSearch.service

import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.enums.*
import me.bfapplicant.domain.repository.ApplicantUserInfoRepository
import me.bfapplicant.domain.repository.ApplicantUserResumeRepository
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
    private val resumeRepository: ApplicantUserResumeRepository,
    private val matchScoreCalculator: MatchScoreCalculator
) {

    fun search(filter: JobSearchFilter, pageable: Pageable): Page<JobPostResponse> {
        val userInfo = resolveUserInfo(filter)
        val resume = resolveRepresentativeResume(filter)

        if (userInfo == null && hasNoFilter(filter)) {
            return jobPostRepository.findAllBy(pageable).map(JobPostResponse::from)
        }

        val envExcludes = buildEnvExcludes(userInfo)
        val educExcludes = buildEducExcludes(resume)
        val careerMonths = resolveCareerMonths(resume)

        if (filter.sortBy == SortType.MATCH_SCORE) {
            return searchWithMatchScore(filter, envExcludes, educExcludes, careerMonths, userInfo, resume, pageable)
        }

        return jobPostQueryRepository
            .search(filter, envExcludes, educExcludes, careerMonths, pageable)
            .map(JobPostResponse::from)
    }

    fun searchWithScore(filter: JobSearchFilter, pageable: Pageable): Page<JobMatchResult> {
        val userInfo = resolveUserInfo(filter)
        val resume = resolveRepresentativeResume(filter)
        val envExcludes = buildEnvExcludes(userInfo)
        val educExcludes = buildEducExcludes(resume)
        val careerMonths = resolveCareerMonths(resume)

        val allMatching = jobPostQueryRepository.search(
            filter.copy(sortBy = SortType.RECENT),
            envExcludes, educExcludes, careerMonths,
            Pageable.unpaged()
        ).content

        val scored = allMatching
            .map { post ->
                val details = matchScoreCalculator.calculate(post, userInfo, resume, filter)
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
        resume: ApplicantUserResume?,
        pageable: Pageable
    ): Page<JobPostResponse> {
        val allMatching = jobPostQueryRepository.search(
            filter, envExcludes, educExcludes, careerMonths, Pageable.unpaged()
        ).content

        val sorted = allMatching
            .map { it to matchScoreCalculator.calculate(it, userInfo, resume, filter).total }
            .sortedByDescending { it.second }
            .map { JobPostResponse.from(it.first) }

        val start = pageable.offset.toInt().coerceAtMost(sorted.size)
        val end = (start + pageable.pageSize).coerceAtMost(sorted.size)
        return PageImpl(sorted.subList(start, end), pageable, sorted.size.toLong())
    }

    private fun resolveUserInfo(filter: JobSearchFilter): ApplicantUserInfo? =
        filter.userId?.let { applicantUserInfoRepository.findByUserUserId(it) }

    private fun resolveRepresentativeResume(filter: JobSearchFilter): ApplicantUserResume? =
        filter.userId?.let { resumeRepository.findByUserUserIdAndIsRepresentativeTrue(it) }

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

    private inline fun <reified T> getExcludedLabels(capability: EnvCondition): List<String>?
        where T : Enum<T>, T : EnvCondition {
        if (capability.level == 0) return null
        return enumValues<T>()
            .filter { it.level > capability.level }
            .map { it.label }
            .ifEmpty { null }
    }

    private fun buildEducExcludes(resume: ApplicantUserResume?): List<String>? {
        if (resume == null || resume.educations.isEmpty()) return null

        val userMaxLevel = resume.educations
            .maxOfOrNull { it.degree.level } ?: return null

        return ReqEduc.entries
            .filter { it.level > userMaxLevel && it.level > 0 }
            .map { it.label }
            .ifEmpty { null }
    }

    private fun resolveCareerMonths(resume: ApplicantUserResume?): Int? {
        if (resume == null || resume.careers.isEmpty()) return null
        return resume.careers.sumOf { it.toMonths() }
    }
}
