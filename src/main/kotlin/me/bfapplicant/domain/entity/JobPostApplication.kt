package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import me.bfapplicant.domain.converter.ResumeSnapshotConverter
import me.bfapplicant.feature.application.dto.ResumeSnapshot
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "job_post_application",
    uniqueConstraints = [UniqueConstraint(columnNames = ["job_post_id", "user_id"])]
)
class JobPostApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "application_id")
    val applicationId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    val jobPost: JobPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val applicant: ApplicantUser,

    @Convert(converter = ResumeSnapshotConverter::class)
    @Column(name = "resume_snapshot", nullable = false, columnDefinition = "jsonb")
    val resumeSnapshot: ResumeSnapshot,

    @Column(name = "applied_at", nullable = false)
    val appliedAt: LocalDateTime = LocalDateTime.now()
)
