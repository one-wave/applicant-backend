package me.bfapplicant.infra.ai

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v2.SpeechClient
import com.google.cloud.speech.v2.SpeechSettings
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class GcpConfig(
    @Value("\${gcp.credentials.path}") private val credentialsPath: String,
    @Value("\${gcp.project-id}") private val projectId: String
) {

    @Bean
    fun gcpCredentials(): GoogleCredentials =
        FileInputStream(credentialsPath).use { GoogleCredentials.fromStream(it) }

    @Bean
    fun speechClient(credentials: GoogleCredentials): SpeechClient {
        val settings = SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
        return SpeechClient.create(settings)
    }

    @Bean
    fun gcpStorage(credentials: GoogleCredentials): Storage =
        StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(credentials)
            .build()
            .service
}
