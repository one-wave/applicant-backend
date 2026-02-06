package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import me.bfapplicant.domain.enums.EnvBothHands
import me.bfapplicant.domain.enums.EnvEyeSight
import me.bfapplicant.domain.enums.EnvHandWork
import me.bfapplicant.domain.enums.EnvLiftPower
import me.bfapplicant.domain.enums.EnvLstnTalk
import me.bfapplicant.domain.enums.EnvStndWalk
import java.time.LocalDate
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
    var userPhone: String? = null,

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_both_hands", nullable = false)
    var envBothHands: EnvBothHands,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_eye_sight", nullable = false)
    var envEyeSight: EnvEyeSight,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_hand_work", nullable = false)
    var envHandWork: EnvHandWork,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_lift_power", nullable = false)
    var envLiftPower: EnvLiftPower,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_lstn_talk", nullable = false)
    var envLstnTalk: EnvLstnTalk,

    @Enumerated(EnumType.STRING)
    @Column(name = "env_stnd_walk", nullable = false)
    var envStndWalk: EnvStndWalk
)
