package me.bfapplicant.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "applicant_user")
class ApplicantUser(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    val userId: UUID? = null,

    @Column(name = "user_name", nullable = false)
    val userName: String,

    @Column(name = "user_email_contact", nullable = false)
    val userEmailContact: String,

    @Column(name = "password", nullable = false)
    val password: String
)
