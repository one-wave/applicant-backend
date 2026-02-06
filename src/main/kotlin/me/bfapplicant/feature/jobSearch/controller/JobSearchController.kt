package me.bfapplicant.feature.jobSearch.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "채용공고 검색", description = "장애인 구직자 맞춤형 채용공고 검색 API")
@RestController
@RequestMapping("/api/job-posts")
class JobSearchController(private val jobSearchService: JobSearchService) {

    @Operation(
        summary = "채용공고 검색",
        description = """
            다양한 필터 조건으로 채용공고를 검색합니다.
            
            - **userId 미전달**: 전체 공고 대상 필터 검색
            - **userId 전달**: 해당 사용자의 신체 환경 조건에 **부적합한 공고를 자동 제외**한 뒤 필터 검색
            - **sortBy=MATCH_SCORE**: 매칭 점수순 정렬 (userId 필수)
            - 만료된 공고(offer_end_dt < 오늘)는 필터 검색 시 자동 제외됩니다.
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "검색 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터", content = [Content()])
        ]
    )
    @GetMapping
    fun search(
        @Parameter(description = "사용자 ID — 전달 시 신체 환경 조건에 부적합한 공고를 자동 제외")
        @RequestParam(required = false) userId: UUID?,

        @Parameter(description = "고용형태 필터 (복수 선택 가능)", example = "상용직")
        @RequestParam(required = false) empTypes: List<String>?,

        @Parameter(description = "급여유형 필터 (복수 선택 가능)", example = "월급")
        @RequestParam(required = false) salaryTypes: List<String>?,

        @Parameter(description = "최소 급여", example = "2000000")
        @RequestParam(required = false) minSalary: BigDecimal?,

        @Parameter(description = "최대 급여", example = "5000000")
        @RequestParam(required = false) maxSalary: BigDecimal?,

        @Parameter(description = "지역 (시/도 단위, 예: '서울', '경기도'). '강원도'↔'강원특별자치도' 등 별칭 자동 처리")
        @RequestParam(required = false) region: String?,

        @Parameter(description = "채용공고명 키워드 (대소문자 무시)")
        @RequestParam(required = false) keyword: String?,

        @Parameter(description = "정렬 기준 — RECENT: 최신순, SALARY_HIGH: 급여높은순, MATCH_SCORE: 매칭점수순(userId 필수)")
        @RequestParam(required = false, defaultValue = "RECENT") sortBy: SortType,

        @Parameter(hidden = true)
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<JobPostResponse> {
        val filter = buildFilter(userId, empTypes, salaryTypes, minSalary, maxSalary, region, keyword, sortBy)
        return jobSearchService.search(filter, pageable)
    }

    @Operation(
        summary = "매칭 점수 기반 채용공고 검색",
        description = """
            사용자의 신체 환경 조건과 대표이력서를 기반으로 적합도 점수(0~100)를 산정하여 높은 점수순으로 반환합니다.
            
            ### 점수 산정 기준 (총 0~100점)
            - **신체환경 적합도 (최대 60점)**: 양손작업·시력·손작업·들기·듣고말하기·서서걷기 6개 항목 × 10점. 사용자 능력이 공고 요구 수준 이상이면 10점, 미달이면 0점. **미달 공고는 결과에서 제외**됩니다.
            - **학력 조건 (최대 15점)**: 대표이력서의 최고 학력이 공고 요구 학력 충족 시 만점
            - **경력 조건 (최대 15점)**: 대표이력서의 총 경력 개월수가 공고 요구 경력 충족 시 만점
            - **선호도 일치 (최대 10점)**: 지역 일치 4점 + 급여유형 일치 3점 + 고용형태 일치 3점
            
            > 대표이력서 미등록 시 학력/경력 점수는 기본 만점(각 15점)으로 산정됩니다. 응답의 `resumeReflected`가 `false`이면 기본값 적용 상태입니다.
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "매칭 검색 성공"),
            ApiResponse(responseCode = "400", description = "userId 누락 또는 잘못된 파라미터", content = [Content()])
        ]
    )
    @GetMapping("/matched")
    fun searchWithScore(
        @Parameter(description = "사용자 ID (필수) — 매칭 점수 산정의 기준", required = true)
        @RequestParam userId: UUID,

        @Parameter(description = "고용형태 필터 (복수 선택 가능)", example = "상용직")
        @RequestParam(required = false) empTypes: List<String>?,

        @Parameter(description = "급여유형 필터 (복수 선택 가능)", example = "월급")
        @RequestParam(required = false) salaryTypes: List<String>?,

        @Parameter(description = "최소 급여", example = "2000000")
        @RequestParam(required = false) minSalary: BigDecimal?,

        @Parameter(description = "최대 급여", example = "5000000")
        @RequestParam(required = false) maxSalary: BigDecimal?,

        @Parameter(description = "지역 (시/도 단위)", example = "서울")
        @RequestParam(required = false) region: String?,

        @Parameter(description = "채용공고명 키워드 (대소문자 무시)")
        @RequestParam(required = false) keyword: String?,

        @Parameter(hidden = true)
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
