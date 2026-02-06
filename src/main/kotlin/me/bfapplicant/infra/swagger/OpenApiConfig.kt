package me.bfapplicant.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.BooleanSchema
import io.swagger.v3.oas.models.media.IntegerSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("장애인 구직자 채용공고 검색 API")
                .version("2.0.0")
                .description(
                    """
                    장애인 구직자를 위한 맞춤형 채용공고 검색 서비스입니다.
                    
                    ## 주요 기능
                    - **회원가입/로그인**: RS256 JWT 기반 인증 (Access Token + Refresh Token 회전)
                    - **이력서 관리**: 대표이력서(HR 검색/매칭용) + 일반이력서(제출용) CRUD
                    - **공고 검색**: 키워드, 지역, 고용형태, 급여 등 다양한 필터로 채용공고를 검색합니다.
                    - **맞춤 매칭**: 사용자의 신체 환경 조건 + 학력/경력을 기반으로, 지원 가능한 공고만 필터링하고 적합도 점수를 산정합니다.
                    
                    ## 매칭 점수 산정 기준 (총 0~100점)
                    | 항목 | 배점 | 산정 방식 |
                    |------|------|-----------|
                    | 신체환경 적합도 | 최대 60점 | 6개 항목(양손, 시력, 손작업, 들기, 듣고말하기, 서서걷기) × 10점. 사용자 능력 ≥ 공고 요구 수준이면 만점, 미달 시 0점. **능력 미달 공고는 검색 결과에서 제외됩니다.** |
                    | 학력 조건 | 최대 15점 | 대표이력서의 최고 학력이 공고 요구 학력을 충족하면 만점 |
                    | 경력 조건 | 최대 15점 | 대표이력서의 총 경력 개월수가 공고 요구 경력을 충족하면 만점 |
                    | 선호도 일치 | 최대 10점 | 지역 일치(4점) + 급여유형 일치(3점) + 고용형태 일치(3점) |
                    
                    > **참고**: 대표이력서 미등록 시 학력/경력 점수는 기본 만점(각 15점)으로 산정됩니다. 응답의 `resumeReflected` 필드가 `false`이면 기본값 적용 상태입니다.
                    """.trimIndent()
                )
        )
        .components(
            Components().addSecuritySchemes(
                "bearer-jwt",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Access Token을 입력하세요 (Bearer 접두사 불필요)")
            )
        )
        .addSecurityItem(SecurityRequirement().addList("bearer-jwt"))

    @Bean
    fun jobPostSearchSchemaCustomizer(): OpenApiCustomizer = OpenApiCustomizer { openApi ->
        fun ref(path: String) = Schema<Any>().apply { `$ref` = path }
        val jobPostRef = "#/components/schemas/JobPostResponse"

        openApi.components.addSchemas(
            "PageJobPostResponse",
            Schema<Any>().apply {
                description = "페이징된 채용공고 응답 (page + size 전달 시)"
                addProperty("content", ArraySchema().items(ref(jobPostRef)))
                addProperty("totalElements", IntegerSchema().format("int64"))
                addProperty("totalPages", IntegerSchema().format("int32"))
                addProperty("size", IntegerSchema().format("int32"))
                addProperty("number", IntegerSchema().format("int32"))
                addProperty("numberOfElements", IntegerSchema().format("int32"))
                addProperty("first", BooleanSchema())
                addProperty("last", BooleanSchema())
                addProperty("empty", BooleanSchema())
                addProperty("pageable", ref("#/components/schemas/PageableObject"))
                addProperty("sort", ref("#/components/schemas/SortObject"))
            }
        )

        openApi.paths["/api/job-posts"]?.get?.responses?.get("200")
            ?.content?.get("application/json")?.schema = Schema<Any>().apply {
            oneOf = listOf(
                ref("#/components/schemas/PageJobPostResponse"),
                ArraySchema().items(ref(jobPostRef)).apply {
                    description = "전체 리스트 응답 (page/size 미전달 시)"
                }
            )
        }
    }
}
