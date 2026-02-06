package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "resume_education")
class ResumeEducation(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "education_id")
    val educationId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    val resume: ApplicantUserResume,

    @Column(name = "school_name", nullable = false)
    val schoolName: String,

    @Column(name = "major")
    val major: String? = null,

    @Column(name = "degree", nullable = false)
    val degree: String,

    @Column(name = "enrollment_date")
    val enrollmentDate: LocalDate? = null,

    @Column(name = "graduation_date")
    val graduationDate: LocalDate? = null,

    @Column(name = "graduation_status", nullable = false)
    val graduationStatus: String
)
