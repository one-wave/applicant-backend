package me.bfapplicant.feature.jobSearch.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.bfapplicant.domain.entity.JobPost
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "채용공고 응답")
data class JobPostResponse(
    @Schema(description = "공고 ID", example = "c3de65de-4909-4161-b6e7-dfa372fe0873")
    val jobPostId: UUID,

    @Schema(description = "회사명", example = "(학교)풍생학원")
    val companyName: String,

    @Schema(description = "회사 연락처", example = "1588-1519", nullable = true)
    val companyPhone: String?,

    @Schema(description = "채용공고명", example = "공공시설 경비원(청사, 학교, 병원 등)")
    val jobNm: String,

    @Schema(description = "근무지 주소", example = "서울특별시 강남구 테헤란로 117")
    val jobLocation: String,

    @Schema(description = "고용형태", example = "상용직", allowableValues = ["상용직", "계약직", "시간제"])
    val empType: String,

    @Schema(description = "입직유형", example = "무관")
    val enterType: String,

    @Schema(description = "요구 학력", example = "무관", allowableValues = ["무관", "고졸", "초대졸", "대졸"])
    val reqEduc: String,

    @Schema(description = "요구 경력 (예: '0년0개월', '무관')", example = "무관")
    val reqCareer: String,

    @Schema(description = "급여 금액", example = "2266780.00")
    val salary: BigDecimal,

    @Schema(description = "급여유형", example = "월급", allowableValues = ["월급", "시급", "일급", "연봉"])
    val salaryType: String,

    @Schema(description = "공고 마감일 (yyyyMMdd 형식)", example = "20260429")
    val offerEndDt: Long,

    @Schema(description = "공고 등록일 (yyyyMMdd 형식)", example = "20260130")
    val regDt: Long
) {
    companion object {
        fun from(post: JobPost) = JobPostResponse(
            jobPostId = post.jobPostId!!,
            companyName = post.company.companyName,
            companyPhone = post.company.companyPhone,
            jobNm = post.jobNm,
            jobLocation = post.jobLocation,
            empType = post.empType,
            enterType = post.enterType,
            reqEduc = post.reqEduc,
            reqCareer = post.reqCareer,
            salary = post.salary,
            salaryType = post.salaryType,
            offerEndDt = post.offerEndDt,
            regDt = post.regDt
        )
    }
}
