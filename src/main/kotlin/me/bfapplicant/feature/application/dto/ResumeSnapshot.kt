package me.bfapplicant.feature.application.dto

import me.bfapplicant.domain.entity.ApplicantUserResume
import me.bfapplicant.domain.entity.ResumeAward
import me.bfapplicant.domain.entity.ResumeCareer
import me.bfapplicant.domain.entity.ResumeCertificate
import me.bfapplicant.domain.entity.ResumeEducation
import me.bfapplicant.domain.entity.ResumeLanguage
import java.time.LocalDate

data class ResumeSnapshot(
    val resumeTitle: String,
    val educations: List<EducationSnapshot>,
    val careers: List<CareerSnapshot>,
    val certificates: List<CertificateSnapshot>,
    val awards: List<AwardSnapshot>,
    val languages: List<LanguageSnapshot>
) {
    companion object {
        fun from(r: ApplicantUserResume) = ResumeSnapshot(
            resumeTitle = r.resumeTitle,
            educations = r.educations.map(EducationSnapshot::from),
            careers = r.careers.map(CareerSnapshot::from),
            certificates = r.certificates.map(CertificateSnapshot::from),
            awards = r.awards.map(AwardSnapshot::from),
            languages = r.languages.map(LanguageSnapshot::from)
        )
    }
}

data class EducationSnapshot(
    val schoolName: String,
    val major: String?,
    val degree: String,
    val enrollmentDate: LocalDate?,
    val graduationDate: LocalDate?,
    val graduationStatus: String
) {
    companion object {
        fun from(e: ResumeEducation) = EducationSnapshot(
            schoolName = e.schoolName,
            major = e.major,
            degree = e.degree.label,
            enrollmentDate = e.enrollmentDate,
            graduationDate = e.graduationDate,
            graduationStatus = e.graduationStatus.label
        )
    }
}

data class CareerSnapshot(
    val companyName: String,
    val department: String?,
    val position: String?,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val description: String?,
    val isCurrentJob: Boolean
) {
    companion object {
        fun from(c: ResumeCareer) = CareerSnapshot(
            companyName = c.companyName,
            department = c.department,
            position = c.position,
            startDate = c.startDate,
            endDate = c.endDate,
            description = c.description,
            isCurrentJob = c.isCurrentJob
        )
    }
}

data class CertificateSnapshot(
    val certificateName: String,
    val issuingOrganization: String?,
    val acquiredDate: LocalDate?
) {
    companion object {
        fun from(c: ResumeCertificate) = CertificateSnapshot(
            certificateName = c.certificateName,
            issuingOrganization = c.issuingOrganization,
            acquiredDate = c.acquiredDate
        )
    }
}

data class AwardSnapshot(
    val awardName: String,
    val issuingOrganization: String?,
    val awardDate: LocalDate?,
    val description: String?
) {
    companion object {
        fun from(a: ResumeAward) = AwardSnapshot(
            awardName = a.awardName,
            issuingOrganization = a.issuingOrganization,
            awardDate = a.awardDate,
            description = a.description
        )
    }
}

data class LanguageSnapshot(
    val languageName: String,
    val testName: String?,
    val score: String?,
    val grade: String?,
    val acquiredDate: LocalDate?
) {
    companion object {
        fun from(l: ResumeLanguage) = LanguageSnapshot(
            languageName = l.languageName,
            testName = l.testName,
            score = l.score,
            grade = l.grade,
            acquiredDate = l.acquiredDate
        )
    }
}
