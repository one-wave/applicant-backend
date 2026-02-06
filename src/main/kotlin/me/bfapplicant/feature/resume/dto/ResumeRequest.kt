package me.bfapplicant.feature.resume.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Schema(description = "이력서 생성/수정 요청")
data class ResumeRequest(
    @field:NotBlank
    @Schema(description = "이력서 제목", example = "2026년 상반기 이력서")
    val resumeTitle: String,

    @Schema(description = "대표이력서 지정 여부", example = "false")
    val isRepresentative: Boolean = false,

    @Schema(description = "학력 목록")
    val educations: List<EducationEntry> = emptyList(),

    @Schema(description = "경력 목록")
    val careers: List<CareerEntry> = emptyList(),

    @Schema(description = "자격증 목록")
    val certificates: List<CertificateEntry> = emptyList(),

    @Schema(description = "수상경력 목록")
    val awards: List<AwardEntry> = emptyList(),

    @Schema(description = "어학능력 목록")
    val languages: List<LanguageEntry> = emptyList()
)

@Schema(description = "학력 항목")
data class EducationEntry(
    @field:NotBlank val schoolName: String,
    val major: String? = null,
    @field:NotBlank val degree: String,
    val enrollmentDate: LocalDate? = null,
    val graduationDate: LocalDate? = null,
    @field:NotBlank val graduationStatus: String
)

@Schema(description = "경력 항목")
data class CareerEntry(
    @field:NotBlank val companyName: String,
    val department: String? = null,
    val position: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val description: String? = null,
    val isCurrentJob: Boolean = false
)

@Schema(description = "자격증 항목")
data class CertificateEntry(
    @field:NotBlank val certificateName: String,
    val issuingOrganization: String? = null,
    val acquiredDate: LocalDate? = null
)

@Schema(description = "수상경력 항목")
data class AwardEntry(
    @field:NotBlank val awardName: String,
    val issuingOrganization: String? = null,
    val awardDate: LocalDate? = null,
    val description: String? = null
)

@Schema(description = "어학능력 항목")
data class LanguageEntry(
    @field:NotBlank val languageName: String,
    val testName: String? = null,
    val score: String? = null,
    val grade: String? = null,
    val acquiredDate: LocalDate? = null
)
