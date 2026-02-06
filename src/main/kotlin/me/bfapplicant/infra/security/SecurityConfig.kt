package me.bfapplicant.infra.security

import me.bfapplicant.infra.security.jwt.JwtAuthenticationFilter
import me.bfapplicant.infra.security.jwt.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(private val jwtAuthenticationFilter: JwtAuthenticationFilter) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/api/auth/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }
}
