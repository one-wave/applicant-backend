package me.bfapplicant.feature.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.JobPostApplication
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "지원 내역 응답")
data class ApplicationResponse(
    val applicationId: UUID,
    val jobPostId: UUID,
    val jobNm: String,
    val companyName: String,
    val applicantName: String,
    val resumeSnapshot: ResumeSnapshot,
    val appliedAt: LocalDateTime
) {
    companion object {
        fun from(e: JobPostApplication) = ApplicationResponse(
            applicationId = e.applicationId!!,
            jobPostId = e.jobPost.jobPostId!!,
            jobNm = e.jobPost.jobNm,
            companyName = e.jobPost.company.companyName,
            applicantName = "${e.applicant.lastName}${e.applicant.firstName}",
            resumeSnapshot = e.resumeSnapshot,
            appliedAt = e.appliedAt
        )
    }
}
