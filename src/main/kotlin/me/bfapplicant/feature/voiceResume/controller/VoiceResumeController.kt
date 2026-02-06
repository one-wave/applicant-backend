package me.bfapplicant.feature.voiceResume.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import me.bfapplicant.feature.voiceResume.dto.VoiceResumeResponse
import me.bfapplicant.feature.voiceResume.service.VoiceResumeService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Tag(name = "음성 이력서", description = "음성으로 이력서를 생성하는 API (시각장애인 보조)")
@RestController
@RequestMapping("/api/resumes/voice")
class VoiceResumeController(private val voiceResumeService: VoiceResumeService) {

    @Operation(
        summary = "음성으로 이력서 생성",
        description = "오디오 파일 업로드 -> GCP STT 텍스트 변환 -> GPT 이력서 JSON 구조화 -> DB 저장",
        responses = [ApiResponse(responseCode = "201", description = "음성 이력서 생성 성공")]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createByVoice(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam("audio") audio: MultipartFile
    ): VoiceResumeResponse = voiceResumeService.processVoiceResume(userId, audio)
}
