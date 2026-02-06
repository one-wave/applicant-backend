package me.bfapplicant.feature.resume.service

import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.ResumeAward
import me.bfapplicant.domain.entity.ResumeCareer
import me.bfapplicant.domain.entity.ResumeCertificate
import me.bfapplicant.domain.entity.ResumeEducation
import me.bfapplicant.domain.entity.ResumeLanguage
import me.bfapplicant.domain.repository.ApplicantUserRepository
import me.bfapplicant.domain.repository.ApplicantUserResumeRepository
import me.bfapplicant.feature.resume.dto.ResumeRequest
import me.bfapplicant.feature.resume.dto.ResumeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class ResumeService(
    private val resumeRepository: ApplicantUserResumeRepository,
    private val userRepository: ApplicantUserRepository
) {

    @Transactional(readOnly = true)
    fun getResumes(userId: UUID): List<ResumeResponse> =
        resumeRepository.findByUserUserId(userId).map(ResumeResponse::from)

    @Transactional(readOnly = true)
    fun getResume(userId: UUID, resumeId: UUID): ResumeResponse =
        ResumeResponse.from(getOwnedResume(userId, resumeId))

    @Transactional
    fun createResume(userId: UUID, req: ResumeRequest): ResumeResponse {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        if (req.isRepresentative) {
            resumeRepository.clearRepresentative(userId)
        }

        val resume = ApplicantUserResume(
            user = user,
            resumeTitle = req.resumeTitle,
            isRepresentative = req.isRepresentative
        )

        populateChildren(resume, req)
        return ResumeResponse.from(resumeRepository.save(resume))
    }

    @Transactional
    fun updateResume(userId: UUID, resumeId: UUID, req: ResumeRequest): ResumeResponse {
        val resume = getOwnedResume(userId, resumeId)

        if (req.isRepresentative && !resume.isRepresentative) {
            resumeRepository.clearRepresentative(userId)
        }

        resume.resumeTitle = req.resumeTitle
        resume.isRepresentative = req.isRepresentative
        resume.updatedAt = LocalDateTime.now()

        resume.educations.clear()
        resume.careers.clear()
        resume.certificates.clear()
        resume.awards.clear()
        resume.languages.clear()

        populateChildren(resume, req)
        return ResumeResponse.from(resumeRepository.save(resume))
    }

    @Transactional
    fun deleteResume(userId: UUID, resumeId: UUID) {
        val resume = getOwnedResume(userId, resumeId)
        resumeRepository.delete(resume)
    }

    @Transactional
    fun setRepresentative(userId: UUID, resumeId: UUID): ResumeResponse {
        val resume = getOwnedResume(userId, resumeId)
        resumeRepository.clearRepresentative(userId)
        resume.isRepresentative = true
        resume.updatedAt = LocalDateTime.now()
        return ResumeResponse.from(resumeRepository.save(resume))
    }

    private fun getOwnedResume(userId: UUID, resumeId: UUID): ApplicantUserResume {
        val resume = resumeRepository.findById(resumeId).orElseThrow {
            IllegalArgumentException("Resume not found")
        }
        require(resume.user.userId == userId) { "Access denied" }
        return resume
    }

    private fun populateChildren(resume: ApplicantUserResume, req: ResumeRequest) {
        req.educations.forEach { e ->
            resume.educations.add(
                ResumeEducation(
                    resume = resume, schoolName = e.schoolName, major = e.major,
                    degree = e.degree, enrollmentDate = e.enrollmentDate,
                    graduationDate = e.graduationDate, graduationStatus = e.graduationStatus
                )
            )
        }
        req.careers.forEach { c ->
            resume.careers.add(
                ResumeCareer(
                    resume = resume, companyName = c.companyName, department = c.department,
                    position = c.position, startDate = c.startDate, endDate = c.endDate,
                    description = c.description, isCurrentJob = c.isCurrentJob
                )
            )
        }
        req.certificates.forEach { c ->
            resume.certificates.add(
                ResumeCertificate(
                    resume = resume, certificateName = c.certificateName,
                    issuingOrganization = c.issuingOrganization, acquiredDate = c.acquiredDate
                )
            )
        }
        req.awards.forEach { a ->
            resume.awards.add(
                ResumeAward(
                    resume = resume, awardName = a.awardName,
                    issuingOrganization = a.issuingOrganization,
                    awardDate = a.awardDate, description = a.description
                )
            )
        }
        req.languages.forEach { l ->
            resume.languages.add(
                ResumeLanguage(
                    resume = resume, languageName = l.languageName, testName = l.testName,
                    score = l.score, grade = l.grade, acquiredDate = l.acquiredDate
                )
            )
        }
    }
}
