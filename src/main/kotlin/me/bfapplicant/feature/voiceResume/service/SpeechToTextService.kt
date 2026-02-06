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
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(javaClass)

    fun transcribe(audioBytes: ByteArray): String {
        val blobName = "temp/${UUID.randomUUID()}.webm"
        val blobId = BlobId.of(bucket, blobName)

        log.info("STT start — audio size: {} bytes, gcs: gs://{}/{}", audioBytes.size, bucket, blobName)

        try {
            storage.create(BlobInfo.newBuilder(blobId).build(), audioBytes)

            val request = BatchRecognizeRequest.newBuilder()
                .setRecognizer("projects/$projectId/locations/global/recognizers/_")
                .setConfig(
                    RecognitionConfig.newBuilder()
                        .setAutoDecodingConfig(AutoDetectDecodingConfig.newBuilder().build())
                        .setModel("latest_long")
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

            log.info("STT response — resultsMap keys: {}", response.resultsMap.keys)

            val fileResult = response.resultsMap.values.firstOrNull()
            log.info("STT fileResult — has inlineResult: {}", fileResult?.hasInlineResult())

            val results = fileResult?.inlineResult?.transcript?.resultsList
            log.info("STT results count: {}, content: {}", results?.size, results)

            return results
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
