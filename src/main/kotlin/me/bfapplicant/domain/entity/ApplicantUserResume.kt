package me.bfapplicant.domain.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "applicant_user_resume")
class ApplicantUserResume(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resume_id")
    val resumeId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: ApplicantUser,

    @Column(name = "resume_title", nullable = false)
    var resumeTitle: String,

    @Column(name = "is_representative", nullable = false)
    var isRepresentative: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "resume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val educations: MutableList<ResumeEducation> = mutableListOf(),

    @OneToMany(mappedBy = "resume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val careers: MutableList<ResumeCareer> = mutableListOf(),

    @OneToMany(mappedBy = "resume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val certificates: MutableList<ResumeCertificate> = mutableListOf(),

    @OneToMany(mappedBy = "resume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val awards: MutableList<ResumeAward> = mutableListOf(),

    @OneToMany(mappedBy = "resume", cascade = [CascadeType.ALL], orphanRemoval = true)
    val languages: MutableList<ResumeLanguage> = mutableListOf()
)
