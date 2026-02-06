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
@Table(name = "resume_language")
class ResumeLanguage(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "language_id")
    val languageId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    val resume: ApplicantUserResume,

    @Column(name = "language_name", nullable = false)
    val languageName: String,

    @Column(name = "test_name")
    val testName: String? = null,

    @Column(name = "score")
    val score: String? = null,

    @Column(name = "grade")
    val grade: String? = null,

    @Column(name = "acquired_date")
    val acquiredDate: LocalDate? = null
)
