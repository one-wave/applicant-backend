package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.ApplicantUserInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ApplicantUserInfoRepository : JpaRepository<ApplicantUserInfo, UUID> {
    fun findByUserUserId(userId: UUID): ApplicantUserInfo?
}
