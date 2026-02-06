package me.bfapplicant.feature.voiceResume.service

import com.google.cloud.speech.v2.AutoDetectDecodingConfig
import com.google.cloud.speech.v2.BatchRecognizeFileMetadata
import com.google.cloud.speech.v2.BatchRecognizeRequest
import com.google.cloud.speech.v2.InlineOutputConfig
import com.google.cloud.speech.v2.RecognitionConfig
import com.google.cloud.speech.v2.RecognitionOutputConfig
import com.google.cloud.speech.v2.SpeechClient
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class SpeechToTextService(
    private val speechClient: SpeechClient,
    private val storage: Storage,
    @Value("\${gcp.project-id}") private val projectId: String,
    @Value("\${gcp.storage.bucket}") private val bucket: String
) {

    fun transcribe(audioBytes: ByteArray): String {
        val blobName = "temp/${UUID.randomUUID()}.webm"
        val blobId = BlobId.of(bucket, blobName)

        try {
            storage.create(BlobInfo.newBuilder(blobId).build(), audioBytes)

            val request = BatchRecognizeRequest.newBuilder()
                .setRecognizer("projects/$projectId/locations/global/recognizers/_")
                .setConfig(
                    RecognitionConfig.newBuilder()
                        .setAutoDecodingConfig(AutoDetectDecodingConfig.newBuilder().build())
                        .addLanguageCodes("ko-KR")
                        .build()
                )
                .addFiles(
                    BatchRecognizeFileMetadata.newBuilder()
                        .setUri("gs://$bucket/$blobName")
                        .build()
                )
                .setRecognitionOutputConfig(
                    RecognitionOutputConfig.newBuilder()
                        .setInlineResponseConfig(InlineOutputConfig.newBuilder().build())
                        .build()
                )
                .build()

            val response = speechClient.batchRecognizeAsync(request)
                .get(5, TimeUnit.MINUTES)

            return response.resultsMap.values
                .firstOrNull()
                ?.inlineResult
                ?.transcript
                ?.resultsList
                ?.mapNotNull { it.alternativesList.firstOrNull() }
                ?.joinToString(" ") { it.transcript }
                ?.trim()
                ?.ifBlank { null }
                ?: throw IllegalStateException("Speech recognition returned empty result")
        } finally {
            try { storage.delete(blobId) } catch (_: Exception) { }
        }
    }
}
