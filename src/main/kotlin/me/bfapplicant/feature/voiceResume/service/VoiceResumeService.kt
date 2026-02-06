package me.bfapplicant.feature.voiceResume.service

import me.bfapplicant.feature.resume.service.ResumeService
import me.bfapplicant.feature.voiceResume.dto.VoiceResumeResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class VoiceResumeService(
    private val speechToTextService: SpeechToTextService,
    private val resumeAiParserService: ResumeAiParserService,
    private val resumeService: ResumeService
) {

    fun processVoiceResume(userId: UUID, audioFile: MultipartFile): VoiceResumeResponse {
        val transcript = speechToTextService.transcribe(audioFile.bytes)
        val resumeRequest = resumeAiParserService.parse(transcript)
        val resume = resumeService.createResume(userId, resumeRequest)
        return VoiceResumeResponse(transcript = transcript, resume = resume)
    }
}
