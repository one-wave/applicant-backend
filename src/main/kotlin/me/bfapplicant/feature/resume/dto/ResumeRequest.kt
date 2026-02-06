package me.bfapplicant.feature.resume.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import me.bfapplicant.domain.enums.EducLevel
import me.bfapplicant.domain.enums.GraduationStatus
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
    @field:NotBlank
    @Schema(description = "학교명", example = "서울대학교")
    val schoolName: String,

    @Schema(description = "전공", example = "컴퓨터공학", nullable = true)
    val major: String? = null,

    @Schema(
        description = "학위 — UNDER_HIGH_SCHOOL: 중졸이하 / HIGH_SCHOOL: 고졸 / ASSOCIATE: 초대졸 / BACHELOR: 대졸 / MASTER: 석사 / DOCTOR: 박사",
        example = "BACHELOR"
    )
    val degree: EducLevel,

    @Schema(description = "입학일", example = "2020-03-01", nullable = true)
    val enrollmentDate: LocalDate? = null,

    @Schema(description = "졸업일", example = "2024-02-28", nullable = true)
    val graduationDate: LocalDate? = null,

    @Schema(
        description = "졸업 상태 — GRADUATED: 졸업 / ENROLLED: 재학중 / ON_LEAVE: 휴학 / DROPPED_OUT: 중퇴 / EXPECTED: 졸업예정",
        example = "GRADUATED"
    )
    val graduationStatus: GraduationStatus
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
