package me.bfapplicant.domain.repository

import me.bfapplicant.domain.entity.WorkAssistantAgency
import org.springframework.data.jpa.repository.JpaRepository

interface WorkAssistantAgencyRepository : JpaRepository<WorkAssistantAgency, Long> {
    fun findByRegion(region: String): List<WorkAssistantAgency>
}
