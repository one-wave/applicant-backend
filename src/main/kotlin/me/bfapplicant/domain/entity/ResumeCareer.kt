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
import java.time.Period
import java.util.UUID

@Entity
@Table(name = "resume_career")
class ResumeCareer(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "career_id")
    val careerId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    val resume: ApplicantUserResume,

    @Column(name = "company_name", nullable = false)
    val companyName: String,

    @Column(name = "department")
    val department: String? = null,

    @Column(name = "position")
    val position: String? = null,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate? = null,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "is_current_job", nullable = false)
    val isCurrentJob: Boolean = false
) {
    fun toMonths(): Int {
        val end = endDate ?: LocalDate.now()
        return Period.between(startDate, end).toTotalMonths().toInt()
    }
}
