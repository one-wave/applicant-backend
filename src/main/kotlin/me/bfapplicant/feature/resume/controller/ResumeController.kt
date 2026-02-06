package me.bfapplicant.feature.resume.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.bfapplicant.feature.resume.dto.ResumeRequest
import me.bfapplicant.feature.resume.dto.ResumeResponse
import me.bfapplicant.feature.resume.service.ResumeService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "이력서", description = "이력서 CRUD API (대표이력서 / 일반이력서)")
@RestController
@RequestMapping("/api/resumes")
class ResumeController(private val resumeService: ResumeService) {

    @Operation(summary = "이력서 목록 조회")
    @GetMapping
    fun list(@AuthenticationPrincipal userId: UUID): List<ResumeResponse> =
        resumeService.getResumes(userId)

    @Operation(summary = "이력서 상세 조회")
    @GetMapping("/{resumeId}")
    fun get(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable resumeId: UUID
    ): ResumeResponse = resumeService.getResume(userId, resumeId)

    @Operation(
        summary = "이력서 생성",
        responses = [ApiResponse(responseCode = "201", description = "생성 성공")]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody req: ResumeRequest
    ): ResumeResponse = resumeService.createResume(userId, req)

    @Operation(summary = "이력서 수정")
    @PutMapping("/{resumeId}")
    fun update(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable resumeId: UUID,
        @Valid @RequestBody req: ResumeRequest
    ): ResumeResponse = resumeService.updateResume(userId, resumeId, req)

    @Operation(summary = "이력서 삭제")
    @DeleteMapping("/{resumeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable resumeId: UUID
    ) = resumeService.deleteResume(userId, resumeId)

    @Operation(
        summary = "대표이력서 교체",
        description = "해당 이력서를 새로운 대표이력서로 교체합니다. 기존 대표이력서는 자동 해제됩니다. " +
            "이력서 생성/수정 시 대표이력서 상태를 직접 변경할 수 없으며, 반드시 이 API를 통해 교체해야 합니다."
    )
    @PatchMapping("/{resumeId}/representative")
    fun swapRepresentative(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable resumeId: UUID
    ): ResumeResponse = resumeService.setRepresentative(userId, resumeId)
}
