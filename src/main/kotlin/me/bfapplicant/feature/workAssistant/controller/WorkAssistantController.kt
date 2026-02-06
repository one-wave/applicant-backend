package me.bfapplicant.feature.workAssistant.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import me.bfapplicant.feature.workAssistant.dto.WorkAssistantResponse
import me.bfapplicant.feature.workAssistant.service.WorkAssistantService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "근로지원인", description = "근로지원인 수행기관 조회 API")
@RestController
@RequestMapping("/api/work-assistants")
class WorkAssistantController(private val service: WorkAssistantService) {

    @Operation(
        summary = "근로지원인 수행기관 조회",
        description = "지역별 근로지원인 수행기관 목록을 조회합니다. region 미전달 시 전체 목록 반환."
    )
    @GetMapping
    fun getAgencies(
        @Parameter(description = "지역 필터 (서울, 부산, 대구, 인천, 광주, 대전, 울산, 세종, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주)")
        @RequestParam(required = false) region: String?
    ): List<WorkAssistantResponse> = service.findByRegion(region)
}
