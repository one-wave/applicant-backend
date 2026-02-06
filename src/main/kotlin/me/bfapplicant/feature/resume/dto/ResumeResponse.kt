package me.bfapplicant.feature.resume.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.ResumeAward
import me.bfapplicant.domain.entity.ResumeCareer
import me.bfapplicant.domain.entity.ResumeCertificate
import me.bfapplicant.domain.entity.ResumeEducation
import me.bfapplicant.domain.entity.ResumeLanguage
import me.bfapplicant.domain.enums.EducLevel
import me.bfapplicant.domain.enums.GraduationStatus
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

@Schema(description = "학력 응답")
data class EducationResponse(
    val educationId: UUID,
    val schoolName: String,
    val major: String?,
    @Schema(description = "학위", example = "BACHELOR")
    val degree: EducLevel,
    @Schema(description = "학위 표시명", example = "대졸")
    val degreeLabel: String,
    val enrollmentDate: LocalDate?,
    val graduationDate: LocalDate?,
    @Schema(description = "졸업 상태", example = "GRADUATED")
    val graduationStatus: GraduationStatus,
    @Schema(description = "졸업 상태 표시명", example = "졸업")
    val graduationStatusLabel: String
) {
    companion object {
        fun from(e: ResumeEducation) = EducationResponse(
            educationId = e.educationId!!,
            schoolName = e.schoolName,
            major = e.major,
            degree = e.degree,
            degreeLabel = e.degree.label,
            enrollmentDate = e.enrollmentDate,
            graduationDate = e.graduationDate,
            graduationStatus = e.graduationStatus,
            graduationStatusLabel = e.graduationStatus.label
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
