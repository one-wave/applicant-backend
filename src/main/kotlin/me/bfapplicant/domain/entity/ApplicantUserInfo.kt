package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "applicant_user_info")
class ApplicantUserInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "applicant_user_info_id")
    val applicantUserInfoId: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: ApplicantUser,

    @Column(name = "user_phone")
    val userPhone: String? = null,

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
    val envStndWalk: String
)
