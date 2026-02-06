package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {

    fun findByTokenHash(hash: String): RefreshToken?

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.tokenHash = :hash AND t.revoked = false")
    fun revokeByHashIfActive(hash: String): Int

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.user.userId = :userId AND t.revoked = false")
    fun revokeAllByUserUserId(userId: UUID): Int
}
