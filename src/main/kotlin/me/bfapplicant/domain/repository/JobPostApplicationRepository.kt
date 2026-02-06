package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.JobPostApplication
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JobPostApplicationRepository : JpaRepository<JobPostApplication, UUID> {

    fun existsByJobPostJobPostIdAndApplicantUserId(jobPostId: UUID, userId: UUID): Boolean

    @EntityGraph(attributePaths = ["jobPost", "jobPost.company"])
    fun findByApplicantUserId(userId: UUID): List<JobPostApplication>
}
