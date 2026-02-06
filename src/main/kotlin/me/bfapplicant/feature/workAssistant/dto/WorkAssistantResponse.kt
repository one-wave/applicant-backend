package me.bfapplicant.feature.workAssistant.dto

import me.bfapplicant.domain.entity.WorkAssistantAgency

data class WorkAssistantResponse(
    val id: Long,
    val branch: String,
    val agencyName: String,
    val address: String,
    val phone: String?,
    val region: String
) {
    companion object {
        fun from(entity: WorkAssistantAgency) = WorkAssistantResponse(
            id = entity.id!!,
            branch = entity.branch,
            agencyName = entity.agencyName,
            address = entity.address,
            phone = entity.phone,
            region = entity.region
        )
    }
}
