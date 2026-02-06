package me.bfapplicant.feature.jobSearch.dto

import me.bfapplicant.domain.enums.SortType
import java.math.BigDecimal
import java.util.UUID

data class JobSearchFilter(
    val userId: UUID? = null,
    val empTypes: List<String>? = null,
    val salaryTypes: List<String>? = null,
    val minSalary: BigDecimal? = null,
    val maxSalary: BigDecimal? = null,
    val region: String? = null,
    val keyword: String? = null,
    val sortBy: SortType = SortType.RECENT
)
