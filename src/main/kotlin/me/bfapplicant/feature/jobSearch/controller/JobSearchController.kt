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
import org.springframework.data.domain.PageRequest
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
            
            ### 페이지네이션
            - **page + size 모두 전달**: `Page` 객체로 페이징 응답 (totalElements, totalPages 등 포함)
            - **page 또는 size 미전달**: 조건에 맞는 전체 공고를 `List`로 한 번에 반환
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "검색 성공 — page/size 전달 시 Page 객체, 미전달 시 List 반환"),
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

        @Parameter(description = "페이지 번호 (0부터 시작). size와 함께 전달해야 페이징 동작. 미전달 시 전체 조회", example = "0")
        @RequestParam(required = false) page: Int?,

        @Parameter(description = "페이지당 항목 수. page와 함께 전달해야 페이징 동작. 미전달 시 전체 조회", example = "20")
        @RequestParam(required = false) size: Int?
    ): Any {
        val filter = buildFilter(userId, empTypes, salaryTypes, minSalary, maxSalary, region, keyword, sortBy)
        val pageable = if (page != null && size != null) PageRequest.of(page, size) else null
        return jobSearchService.search(filter, pageable)
    }

    @Operation(
        summary = "매칭 점수 기반 채용공고 검색",
        description = """
            사용자의 신체 환경 조건과 대표이력서를 기반으로 적합도 점수(0~100)를 산정하여 높은 점수순으로 반환합니다.
            
            ### 점수 산정 기준 (총 0~100점)
            - **신체환경 적합도 (최대 18점)**: 6개 항목 × 3점. **미달 공고는 결과에서 제외**됩니다.
            - **급여 매력도 (최대 25점)**: 검색 결과 내 월 환산 급여 상대 순위(percentile) 기반
            - **고용 안정성 (최대 12점)**: 상용직 12 / 계약직 7 / 시간제 3
            - **학력·경력 적합도 (최대 15점)**: 학력 8 + 경력 7, 초과 충족 시 가점
            - **선호도 일치 (최대 15점)**: 지역 6 + 급여유형 4 + 고용형태 5
            - **공고 신선도 (최대 15점)**: 최근 등록 공고에 가점
            
            > 대표이력서 미등록 시 학력·경력은 기본 중간값으로 산정됩니다. 응답의 `resumeReflected`가 `false`이면 기본값 적용 상태입니다.
            
            ### 페이지네이션 (기본 20개)
            - **page**: 페이지 번호 (0부터 시작, 기본값 0)
            - **size**: 페이지당 항목 수 (기본값 20)
            - 응답에 totalElements, totalPages, number 등 페이징 메타 정보 포함
        """,
        responses = [
            ApiResponse(responseCode = "200", description = "매칭 검색 성공 — Page 객체 (totalElements, totalPages 등 포함)"),
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

        @Parameter(description = "페이지 번호 (0부터 시작, 기본값 0)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,

        @Parameter(description = "페이지당 항목 수 (기본값 20)", example = "20")
        @RequestParam(required = false, defaultValue = "20") size: Int
    ): Page<JobMatchResult> {
        val filter = buildFilter(userId, empTypes, salaryTypes, minSalary, maxSalary, region, keyword, SortType.MATCH_SCORE)
        return jobSearchService.searchWithScore(filter, PageRequest.of(page, size))
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
