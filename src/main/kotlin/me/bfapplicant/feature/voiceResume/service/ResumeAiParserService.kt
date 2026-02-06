package me.bfapplicant.feature.voiceResume.service

import tools.jackson.databind.ObjectMapper
import me.bfapplicant.feature.resume.dto.ResumeRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class ResumeAiParserService(
    @Qualifier("openAiRestClient") private val restClient: RestClient,
    private val objectMapper: ObjectMapper,
    @Value("\${openai.model}") private val model: String
) {

    fun parse(transcript: String): ResumeRequest {
        val body = mapOf(
            "model" to model,
            "messages" to listOf(
                mapOf("role" to "system", "content" to SYSTEM_PROMPT),
                mapOf("role" to "user", "content" to transcript)
            ),
            "temperature" to 0.1,
            "response_format" to mapOf("type" to "json_object")
        )

        val response = restClient.post()
            .uri("/chat/completions")
            .body(body)
            .retrieve()
            .body(Map::class.java)
            ?: throw IllegalStateException("OpenAI returned null response")

        return objectMapper.readValue(extractContent(response), ResumeRequest::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractContent(response: Map<*, *>): String {
        val choices = response["choices"] as? List<Map<String, Any>>
            ?: throw IllegalStateException("No choices in OpenAI response")
        val message = choices.first()["message"] as Map<String, Any>
        return message["content"] as String
    }

    companion object {
        private val SYSTEM_PROMPT = """
            You are a resume data extractor for visually impaired Korean users.
            The user will dictate their resume information in Korean.
            Extract and return a JSON object matching this exact schema:

            {
              "resumeTitle": "string (generate a descriptive Korean title)",
              "isRepresentative": false,
              "educations": [{
                "schoolName": "string",
                "major": "string or null",
                "degree": "enum: UNDER_HIGH_SCHOOL | HIGH_SCHOOL | ASSOCIATE | BACHELOR | MASTER | DOCTOR",
                "enrollmentDate": "YYYY-MM-DD or null",
                "graduationDate": "YYYY-MM-DD or null",
                "graduationStatus": "enum: GRADUATED | ENROLLED | ON_LEAVE | DROPPED_OUT | EXPECTED"
              }],
              "careers": [{
                "companyName": "string",
                "department": "string or null",
                "position": "string or null",
                "startDate": "YYYY-MM-DD",
                "endDate": "YYYY-MM-DD or null",
                "description": "string or null",
                "isCurrentJob": boolean
              }],
              "certificates": [{
                "certificateName": "string",
                "issuingOrganization": "string or null",
                "acquiredDate": "YYYY-MM-DD or null"
              }],
              "awards": [{
                "awardName": "string",
                "issuingOrganization": "string or null",
                "awardDate": "YYYY-MM-DD or null",
                "description": "string or null"
              }],
              "languages": [{
                "languageName": "string",
                "testName": "string or null",
                "score": "string or null",
                "grade": "string or null",
                "acquiredDate": "YYYY-MM-DD or null"
              }]
            }

            Rules:
            - Return ONLY valid JSON. No markdown, no explanation.
            - For partial dates: "2020년" -> "2020-01-01", "2020년 3월" -> "2020-03-01".
            - If currently employed, set isCurrentJob=true and endDate=null.
            - Use empty arrays [] for sections with no mentioned information.
            - Generate a descriptive resumeTitle in Korean based on the content.
        """.trimIndent()
    }
}
