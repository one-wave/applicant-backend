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
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refresh_token_id")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: ApplicantUser,

    @Column(name = "token_hash", nullable = false, unique = true)
    val tokenHash: String,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime,

    @Column(name = "revoked", nullable = false)
    var revoked: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
