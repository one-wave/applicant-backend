package me.bfapplicant.infra.ai

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.Duration

@Configuration
class OpenAiConfig(
    @Value("\${openai.api-key}") private val apiKey: String
) {

    @Bean
    fun openAiRestClient(): RestClient {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(Duration.ofSeconds(10))
            setReadTimeout(Duration.ofSeconds(60))
        }
        return RestClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .requestFactory(factory)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}
