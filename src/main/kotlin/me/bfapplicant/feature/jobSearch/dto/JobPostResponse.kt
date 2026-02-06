package me.bfapplicant.feature.jobSearch.dto

import me.bfapplicant.domain.entity.JobPost
import java.math.BigDecimal
import java.util.UUID

data class JobPostResponse(
    val jobPostId: UUID,
    val companyName: String,
    val companyPhone: String?,
    val jobNm: String,
    val jobLocation: String,
    val empType: String,
    val enterType: String,
    val reqEduc: String,
    val reqCareer: String,
    val salary: BigDecimal,
    val salaryType: String,
    val offerEndDt: Long,
    val regDt: Long
) {
    companion object {
        fun from(post: JobPost) = JobPostResponse(
            jobPostId = post.jobPostId!!,
            companyName = post.company.companyName,
            companyPhone = post.company.companyPhone,
            jobNm = post.jobNm,
            jobLocation = post.jobLocation,
            empType = post.empType,
            enterType = post.enterType,
            reqEduc = post.reqEduc,
            reqCareer = post.reqCareer,
            salary = post.salary,
            salaryType = post.salaryType,
            offerEndDt = post.offerEndDt,
            regDt = post.regDt
        )
    }
}
