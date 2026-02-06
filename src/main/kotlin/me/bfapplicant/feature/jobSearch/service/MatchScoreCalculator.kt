package me.bfapplicant.feature.jobSearch.service

import me.bfapplicant.domain.entity.ApplicantUserInfo
import me.bfapplicant.domain.entity.JobPost
import me.bfapplicant.domain.enums.*
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import me.bfapplicant.feature.jobSearch.dto.MatchDetails
import org.springframework.stereotype.Component

@Component
class MatchScoreCalculator {

    fun calculate(post: JobPost, userInfo: ApplicantUserInfo?, filter: JobSearchFilter): MatchDetails {
        val envScore = calculateEnvScore(post, userInfo)
        val educScore = calculateEducScore(post, userInfo)
        val careerScore = calculateCareerScore(post, userInfo)
        val preferenceScore = calculatePreferenceScore(post, filter)
        return MatchDetails(envScore, educScore, careerScore, preferenceScore)
    }

    private fun calculateEnvScore(post: JobPost, userInfo: ApplicantUserInfo?): Int {
        if (userInfo == null) return MatchDetails.ENV_MAX

        var score = 0
        score += fieldScore(EnvBothHands.fromLabel(userInfo.envBothHands), EnvBothHands.fromLabel(post.envBothHands))
        score += fieldScore(EnvEyeSight.fromLabel(userInfo.envEyeSight), EnvEyeSight.fromLabel(post.envEyeSight))
        score += fieldScore(EnvHandWork.fromLabel(userInfo.envHandWork), EnvHandWork.fromLabel(post.envHandWork))
        score += fieldScore(EnvLiftPower.fromLabel(userInfo.envLiftPower), EnvLiftPower.fromLabel(post.envLiftPower))
        score += fieldScore(EnvLstnTalk.fromLabel(userInfo.envLstnTalk), EnvLstnTalk.fromLabel(post.envLstnTalk))
        score += fieldScore(EnvStndWalk.fromLabel(userInfo.envStndWalk), EnvStndWalk.fromLabel(post.envStndWalk))
        return score
    }

    private fun fieldScore(userCap: EnvCondition, jobReq: EnvCondition): Int = when {
        jobReq.level == 0 -> MatchDetails.ENV_PER_FIELD
        userCap.level == 0 -> MatchDetails.ENV_PER_FIELD
        userCap.level >= jobReq.level -> MatchDetails.ENV_PER_FIELD
        else -> 0
    }

    private fun calculateEducScore(post: JobPost, userInfo: ApplicantUserInfo?): Int {
        if (userInfo == null) return MatchDetails.EDUC_MAX
        val jobReq = ReqEduc.fromLabel(post.reqEduc)
        if (jobReq.level == 0) return MatchDetails.EDUC_MAX
        // TODO: compare with user's education when field is available
        return MatchDetails.EDUC_MAX
    }

    private fun calculateCareerScore(post: JobPost, userInfo: ApplicantUserInfo?): Int {
        if (userInfo == null) return MatchDetails.CAREER_MAX
        val reqMonths = CareerParser.toMonths(post.reqCareer)
        if (reqMonths == 0) return MatchDetails.CAREER_MAX
        // TODO: compare with user's career months when field is available
        return MatchDetails.CAREER_MAX
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
