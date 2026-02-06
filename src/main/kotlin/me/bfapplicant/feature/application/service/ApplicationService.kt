package me.bfapplicant.feature.application.service

import me.bfapplicant.domain.entity.JobPostApplication
import me.bfapplicant.domain.repository.ApplicantUserResumeRepository
import me.bfapplicant.domain.repository.JobPostApplicationRepository
import me.bfapplicant.domain.repository.JobPostRepository
import me.bfapplicant.feature.application.dto.ApplicationResponse
import me.bfapplicant.feature.application.dto.ApplyRequest
import me.bfapplicant.feature.application.dto.ResumeSnapshot
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class ApplicationService(
    private val applicationRepository: JobPostApplicationRepository,
    private val jobPostRepository: JobPostRepository,
    private val resumeRepository: ApplicantUserResumeRepository
) {

    @Transactional
    fun apply(userId: UUID, req: ApplyRequest): ApplicationResponse {
        val jobPost = jobPostRepository.findById(req.jobPostId).orElseThrow {
            IllegalArgumentException("Job post not found")
        }

        val today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toLong()
        require(jobPost.offerEndDt >= today) { "This job post has expired" }

        val resume = resumeRepository.findById(req.resumeId).orElseThrow {
            IllegalArgumentException("Resume not found")
        }
        require(resume.user.userId == userId) { "Access denied" }

        require(!applicationRepository.existsByJobPostJobPostIdAndApplicantUserId(req.jobPostId, userId)) {
            "Already applied to this job post"
        }

        val application = try {
            applicationRepository.save(
                JobPostApplication(
                    jobPost = jobPost,
                    applicant = resume.user,
                    resumeSnapshot = ResumeSnapshot.from(resume)
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw IllegalStateException("Already applied to this job post")
        }

        return ApplicationResponse.from(application)
    }

    @Transactional(readOnly = true)
    fun getMyApplications(userId: UUID): List<ApplicationResponse> =
        applicationRepository.findByApplicantUserId(userId).map(ApplicationResponse::from)
}
