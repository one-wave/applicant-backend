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
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "job_post")
class JobPost(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_post_id")
    val jobPostId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,

    @Column(name = "job_email_contact")
    val jobEmailContact: String? = null,

    @Column(name = "job_location", nullable = false)
    val jobLocation: String,

    @Column(name = "emp_type", nullable = false)
    val empType: String,

    @Column(name = "enter_type", nullable = false)
    val enterType: String,

    @Column(name = "env_both_hands", nullable = false)
    val envBothHands: String,

    @Column(name = "env_eye_sight", nullable = false)
    val envEyeSight: String,

    @Column(name = "env_hand_work", nullable = false)
    val envHandWork: String,

    @Column(name = "env_lift_power", nullable = false)
    val envLiftPower: String,

    @Column(name = "env_lstn_talk", nullable = false)
    val envLstnTalk: String,

    @Column(name = "env_stnd_walk", nullable = false)
    val envStndWalk: String,

    @Column(name = "job_nm", nullable = false)
    val jobNm: String,

    @Column(name = "offer_end_dt", nullable = false)
    val offerEndDt: Long,

    @Column(name = "reg_dt", nullable = false)
    val regDt: Long,

    @Column(name = "regagn_name", nullable = false)
    val regagnName: String,

    @Column(name = "req_career", nullable = false)
    val reqCareer: String,

    @Column(name = "req_educ", nullable = false)
    val reqEduc: String,

    @Column(name = "salary", nullable = false)
    val salary: BigDecimal,

    @Column(name = "salary_type", nullable = false)
    val salaryType: String,

    @Column(name = "term_date", nullable = false)
    val termDate: LocalDate
)
