package me.bfapplicant.feature.voiceResume.dto

import me.bfapplicant.feature.resume.dto.ResumeResponse

data class VoiceResumeResponse(
    val transcript: String,
    val resume: ResumeResponse
)
