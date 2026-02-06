package me.bfapplicant.feature.workAssistant.service

import me.bfapplicant.domain.repository.WorkAssistantAgencyRepository
import me.bfapplicant.feature.workAssistant.dto.WorkAssistantResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkAssistantService(private val repository: WorkAssistantAgencyRepository) {

    fun findByRegion(region: String?): List<WorkAssistantResponse> =
        if (region.isNullOrBlank()) repository.findAll().map(WorkAssistantResponse::from)
        else repository.findByRegion(region).map(WorkAssistantResponse::from)
}
