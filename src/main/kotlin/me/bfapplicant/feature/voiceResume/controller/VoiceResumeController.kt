package me.bfapplicant.feature.voiceResume.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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

@Tag(
    name = "음성 이력서",
    description = "시각장애인을 위한 음성 기반 이력서 생성 API. " +
        "브라우저에서 녹음한 오디오를 업로드하면 GCP STT로 텍스트 변환 후, GPT가 이력서 JSON으로 구조화하여 저장합니다."
)
@RestController
@RequestMapping("/api/resumes/voice")
class VoiceResumeController(private val voiceResumeService: VoiceResumeService) {

    @Operation(
        summary = "음성으로 이력서 생성",
        description = """
오디오 파일을 업로드하면 다음 파이프라인이 실행됩니다:
1. GCP Speech-to-Text V2 (BatchRecognize)로 한국어 텍스트 변환
2. OpenAI GPT로 텍스트를 이력서 JSON 구조로 파싱
3. 파싱된 이력서를 DB에 저장

**오디오 포맷**: `audio/webm` (브라우저 MediaRecorder 기본 출력). WAV, FLAC, OGG 등도 지원 (GCP AutoDetect).
**최대 파일 크기**: 10MB
**예상 처리 시간**: 10~60초 (오디오 길이에 따라 다름)

프론트엔드 전송 예시:
```
const formData = new FormData();
formData.append('audio', audioBlob, 'recording.webm');
fetch('/api/resumes/voice', {
  method: 'POST',
  headers: { 'Authorization': 'Bearer {accessToken}' },
  body: formData
});
```""",
        responses = [
            ApiResponse(responseCode = "201", description = "음성 이력서 생성 성공 — transcript(STT 결과)와 생성된 이력서를 함께 반환"),
            ApiResponse(responseCode = "401", description = "인증 실패 (JWT 토큰 누락/만료)"),
            ApiResponse(responseCode = "500", description = "STT 변환 실패 또는 GPT 파싱 실패")
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createByVoice(
        @AuthenticationPrincipal userId: UUID,
        @Parameter(
            description = "녹음된 오디오 파일 (audio/webm 권장, 최대 10MB)",
            required = true
        )
        @RequestParam("audio") audio: MultipartFile
    ): VoiceResumeResponse = voiceResumeService.processVoiceResume(userId, audio)
}
