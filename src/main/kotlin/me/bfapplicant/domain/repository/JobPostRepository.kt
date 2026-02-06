package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.JobPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface JobPostRepository : JpaRepository<JobPost, UUID> {

    @EntityGraph(attributePaths = ["company"])
    fun findAllBy(pageable: Pageable): Page<JobPost>

    @EntityGraph(attributePaths = ["company"])
    @Query("SELECT j FROM JobPost j")
    fun findAllWithCompany(): List<JobPost>
}
