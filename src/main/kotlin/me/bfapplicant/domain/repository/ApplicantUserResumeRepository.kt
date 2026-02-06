package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.ApplicantUserResume
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface ApplicantUserResumeRepository : JpaRepository<ApplicantUserResume, UUID> {

    fun findByUserUserId(userId: UUID): List<ApplicantUserResume>

    fun findByUserUserIdAndIsRepresentativeTrue(userId: UUID): ApplicantUserResume?

    fun countByUserUserId(userId: UUID): Long

    @Modifying
    @Query("UPDATE ApplicantUserResume r SET r.isRepresentative = false WHERE r.user.userId = :userId AND r.isRepresentative = true")
    fun clearRepresentative(userId: UUID): Int
}
