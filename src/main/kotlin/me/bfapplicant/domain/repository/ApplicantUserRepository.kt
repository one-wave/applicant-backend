package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.ApplicantUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ApplicantUserRepository : JpaRepository<ApplicantUser, UUID> {
    fun findByUserEmailContact(email: String): ApplicantUser?
}
