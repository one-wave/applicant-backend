package me.bfapplicant.feature.jobSearch.service

import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.JobPost
import me.bfapplicant.domain.enums.*
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import me.bfapplicant.feature.jobSearch.dto.MatchDetails
import org.springframework.stereotype.Component

@Component
class MatchScoreCalculator {

    fun calculate(
        post: JobPost,
        userInfo: ApplicantUserInfo?,
        resume: ApplicantUserResume?,
        filter: JobSearchFilter
    ): MatchDetails {
        val resumeReflected = hasResumeData(resume)
        return MatchDetails(
            envScore = calculateEnvScore(post, userInfo),
            educScore = calculateEducScore(post, resume, resumeReflected),
            careerScore = calculateCareerScore(post, resume, resumeReflected),
            preferenceScore = calculatePreferenceScore(post, filter),
            resumeReflected = resumeReflected
        )
    }

    private fun hasResumeData(resume: ApplicantUserResume?): Boolean =
        resume != null && (resume.educations.isNotEmpty() || resume.careers.isNotEmpty())

    private fun calculateEnvScore(post: JobPost, userInfo: ApplicantUserInfo?): Int {
        if (userInfo == null) return MatchDetails.ENV_MAX

        var score = 0
        score += fieldScore(userInfo.envBothHands, EnvBothHands.fromLabel(post.envBothHands))
        score += fieldScore(userInfo.envEyeSight, EnvEyeSight.fromLabel(post.envEyeSight))
        score += fieldScore(userInfo.envHandWork, EnvHandWork.fromLabel(post.envHandWork))
        score += fieldScore(userInfo.envLiftPower, EnvLiftPower.fromLabel(post.envLiftPower))
        score += fieldScore(userInfo.envLstnTalk, EnvLstnTalk.fromLabel(post.envLstnTalk))
        score += fieldScore(userInfo.envStndWalk, EnvStndWalk.fromLabel(post.envStndWalk))
        return score
    }

    private fun fieldScore(userCap: EnvCondition, jobReq: EnvCondition): Int = when {
        jobReq.level == 0 -> MatchDetails.ENV_PER_FIELD
        userCap.level == 0 -> 0
        userCap.level >= jobReq.level -> MatchDetails.ENV_PER_FIELD
        else -> 0
    }

    private fun calculateEducScore(post: JobPost, resume: ApplicantUserResume?, reflected: Boolean): Int {
        val jobReq = ReqEduc.fromLabel(post.reqEduc)
        if (jobReq.level == 0) return MatchDetails.EDUC_MAX
        if (!reflected) return MatchDetails.EDUC_MAX

        val userMaxLevel = resume!!.educations
            .maxOfOrNull { it.degree.level } ?: 0
        return if (userMaxLevel >= jobReq.level) MatchDetails.EDUC_MAX else 0
    }

    private fun calculateCareerScore(post: JobPost, resume: ApplicantUserResume?, reflected: Boolean): Int {
        val reqMonths = CareerParser.toMonths(post.reqCareer)
        if (reqMonths == 0) return MatchDetails.CAREER_MAX
        if (!reflected) return MatchDetails.CAREER_MAX

        val userMonths = resume!!.careers.sumOf { it.toMonths() }
        return if (userMonths >= reqMonths) MatchDetails.CAREER_MAX else 0
    }

    private fun calculatePreferenceScore(post: JobPost, filter: JobSearchFilter): Int {
        var score = 0
        if (filter.region != null) {
            val variants = Region.normalize(filter.region)
            if (variants.any { post.jobLocation.startsWith(it) }) score += 4
        }
        if (filter.salaryTypes != null && post.salaryType in filter.salaryTypes) score += 3
        if (filter.empTypes != null && post.empType in filter.empTypes) score += 3
        return score
    }
}
