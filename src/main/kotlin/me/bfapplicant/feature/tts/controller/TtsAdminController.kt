package me.bfapplicant.feature.tts.controller

import me.bfapplicant.feature.tts.dto.TtsGenerateResponse
import me.bfapplicant.feature.tts.service.TtsService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tts")
class TtsAdminController(private val ttsService: TtsService) {

    @PostMapping("/admin/generate")
    fun generate(
        @RequestParam(defaultValue = "false") forceRegenerate: Boolean
    ): ResponseEntity<TtsGenerateResponse> {
        val urls = ttsService.generateAll(forceRegenerate)
        return ResponseEntity.ok(TtsGenerateResponse(urls))
    }

    @GetMapping("/guides/{key}")
    fun streamGuide(@PathVariable key: String): ResponseEntity<ByteArray> {
        val audio = ttsService.readFromGcs(key)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .header("Cache-Control", "public, max-age=86400")
            .body(audio)
    }
}
