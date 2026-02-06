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
@Table(name = "resume_award")
class ResumeAward(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "award_id")
    val awardId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    val resume: ApplicantUserResume,

    @Column(name = "award_name", nullable = false)
    val awardName: String,

    @Column(name = "issuing_organization")
    val issuingOrganization: String? = null,

    @Column(name = "award_date")
    val awardDate: LocalDate? = null,

    @Column(name = "description")
    val description: String? = null
)
