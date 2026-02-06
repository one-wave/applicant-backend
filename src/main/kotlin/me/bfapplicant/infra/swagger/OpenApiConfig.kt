package me.bfapplicant.infra.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("장애인 구직자 채용공고 검색 API")
                .version("1.0.0")
                .description(
                    """
                    장애인 구직자를 위한 맞춤형 채용공고 검색 서비스입니다.
                    
                    ## 주요 기능
                    - **공고 검색**: 키워드, 지역, 고용형태, 급여 등 다양한 필터로 채용공고를 검색합니다.
                    - **맞춤 매칭**: 사용자의 신체 환경 조건을 기반으로, 지원 가능한 공고만 필터링하고 적합도 점수를 산정합니다.
                    
                    ## 매칭 점수 산정 기준 (총 0~100점)
                    | 항목 | 배점 | 산정 방식 |
                    |------|------|-----------|
                    | 신체환경 적합도 | 최대 60점 | 6개 항목(양손, 시력, 손작업, 들기, 듣고말하기, 서서걷기) × 10점. 사용자 능력 ≥ 공고 요구 수준이면 만점, 미달 시 0점. **능력 미달 공고는 검색 결과에서 제외됩니다.** |
                    | 학력 조건 | 최대 15점 | 공고 요구 학력을 충족하면 만점 |
                    | 경력 조건 | 최대 15점 | 공고 요구 경력을 충족하면 만점 |
                    | 선호도 일치 | 최대 10점 | 지역 일치(4점) + 급여유형 일치(3점) + 고용형태 일치(3점) |
                    """.trimIndent()
                )
        )
}
