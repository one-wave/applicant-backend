package me.bfapplicant.feature.jobSearch.controller

import me.bfapplicant.domain.enums.SortType
import me.bfapplicant.feature.jobSearch.dto.JobMatchResult
import me.bfapplicant.feature.jobSearch.dto.JobPostResponse
import me.bfapplicant.feature.jobSearch.dto.JobSearchFilter
import me.bfapplicant.feature.jobSearch.service.JobSearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/job-posts")
class JobSearchController(private val jobSearchService: JobSearchService) {

    @GetMapping
    fun search(
        @RequestParam(required = false) userId: UUID?,
        @RequestParam(required = false) empTypes: List<String>?,
        @RequestParam(required = false) salaryTypes: List<String>?,
        @RequestParam(required = false) minSalary: BigDecimal?,
        @RequestParam(required = false) maxSalary: BigDecimal?,
        @RequestParam(required = false) region: String?,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false, defaultValue = "RECENT") sortBy: SortType,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<JobPostResponse> {
        val filter = buildFilter(userId, empTypes, salaryTypes, minSalary, maxSalary, region, keyword, sortBy)
        return jobSearchService.search(filter, pageable)
    }

    @GetMapping("/matched")
    fun searchWithScore(
        @RequestParam userId: UUID,
        @RequestParam(required = false) empTypes: List<String>?,
        @RequestParam(required = false) salaryTypes: List<String>?,
        @RequestParam(required = false) minSalary: BigDecimal?,
        @RequestParam(required = false) maxSalary: BigDecimal?,
        @RequestParam(required = false) region: String?,
        @RequestParam(required = false) keyword: String?,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<JobMatchResult> {
        val filter = buildFilter(userId, empTypes, salaryTypes, minSalary, maxSalary, region, keyword, SortType.MATCH_SCORE)
        return jobSearchService.searchWithScore(filter, pageable)
    }

    private fun buildFilter(
        userId: UUID?,
        empTypes: List<String>?,
        salaryTypes: List<String>?,
        minSalary: BigDecimal?,
        maxSalary: BigDecimal?,
        region: String?,
        keyword: String?,
        sortBy: SortType
    ) = JobSearchFilter(
        userId = userId,
        empTypes = empTypes,
        salaryTypes = salaryTypes,
        minSalary = minSalary,
        maxSalary = maxSalary,
        region = region,
        keyword = keyword,
        sortBy = sortBy
    )
}
