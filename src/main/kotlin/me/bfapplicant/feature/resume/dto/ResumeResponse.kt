package me.bfapplicant.feature.resume.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.ResumeAward
import me.bfapplicant.domain.entity.ResumeCareer
import me.bfapplicant.domain.entity.ResumeCertificate
import me.bfapplicant.domain.entity.ResumeEducation
import me.bfapplicant.domain.entity.ResumeLanguage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "이력서 응답")
data class ResumeResponse(
    val resumeId: UUID,
    val resumeTitle: String,
    val isRepresentative: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val educations: List<EducationResponse>,
    val careers: List<CareerResponse>,
    val certificates: List<CertificateResponse>,
    val awards: List<AwardResponse>,
    val languages: List<LanguageResponse>
) {
    companion object {
        fun from(r: ApplicantUserResume) = ResumeResponse(
            resumeId = r.resumeId!!,
            resumeTitle = r.resumeTitle,
            isRepresentative = r.isRepresentative,
            createdAt = r.createdAt,
            updatedAt = r.updatedAt,
            educations = r.educations.map(EducationResponse::from),
            careers = r.careers.map(CareerResponse::from),
            certificates = r.certificates.map(CertificateResponse::from),
            awards = r.awards.map(AwardResponse::from),
            languages = r.languages.map(LanguageResponse::from)
        )
    }
}

data class EducationResponse(
    val educationId: UUID, val schoolName: String, val major: String?,
    val degree: String, val enrollmentDate: LocalDate?, val graduationDate: LocalDate?,
    val graduationStatus: String
) {
    companion object {
        fun from(e: ResumeEducation) = EducationResponse(
            e.educationId!!, e.schoolName, e.major, e.degree,
            e.enrollmentDate, e.graduationDate, e.graduationStatus
        )
    }
}

data class CareerResponse(
    val careerId: UUID, val companyName: String, val department: String?,
    val position: String?, val startDate: LocalDate, val endDate: LocalDate?,
    val description: String?, val isCurrentJob: Boolean
) {
    companion object {
        fun from(c: ResumeCareer) = CareerResponse(
            c.careerId!!, c.companyName, c.department, c.position,
            c.startDate, c.endDate, c.description, c.isCurrentJob
        )
    }
}

data class CertificateResponse(
    val certificateId: UUID, val certificateName: String,
    val issuingOrganization: String?, val acquiredDate: LocalDate?
) {
    companion object {
        fun from(c: ResumeCertificate) = CertificateResponse(
            c.certificateId!!, c.certificateName, c.issuingOrganization, c.acquiredDate
        )
    }
}

data class AwardResponse(
    val awardId: UUID, val awardName: String,
    val issuingOrganization: String?, val awardDate: LocalDate?, val description: String?
) {
    companion object {
        fun from(a: ResumeAward) = AwardResponse(
            a.awardId!!, a.awardName, a.issuingOrganization, a.awardDate, a.description
        )
    }
}

data class LanguageResponse(
    val languageId: UUID, val languageName: String,
    val testName: String?, val score: String?, val grade: String?, val acquiredDate: LocalDate?
) {
    companion object {
        fun from(l: ResumeLanguage) = LanguageResponse(
            l.languageId!!, l.languageName, l.testName, l.score, l.grade, l.acquiredDate
        )
    }
}
