package me.bfapplicant.feature.application.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.bfapplicant.feature.application.dto.ApplicationResponse
import me.bfapplicant.feature.application.dto.ApplyRequest
import me.bfapplicant.feature.application.service.ApplicationService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "지원", description = "공고 지원 및 지원 내역 조회 API")
@RestController
@RequestMapping("/api/applications")
class ApplicationController(private val applicationService: ApplicationService) {

    @Operation(
        summary = "공고 지원",
        description = "선택한 이력서로 공고에 지원합니다. 이력서 데이터는 스냅샷으로 저장됩니다.",
        responses = [
            ApiResponse(responseCode = "201", description = "지원 성공"),
            ApiResponse(responseCode = "400", description = "만료된 공고, 중복 지원, 또는 유효성 검증 실패")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun apply(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody req: ApplyRequest
    ): ApplicationResponse = applicationService.apply(userId, req)

    @Operation(
        summary = "내 지원 내역 조회",
        responses = [ApiResponse(responseCode = "200", description = "조회 성공")]
    )
    @GetMapping("/me")
    fun getMyApplications(
        @AuthenticationPrincipal userId: UUID
    ): List<ApplicationResponse> = applicationService.getMyApplications(userId)
}
