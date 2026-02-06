package me.bfapplicant.feature.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.bfapplicant.feature.auth.dto.LoginRequest
import me.bfapplicant.feature.auth.dto.RefreshRequest
import me.bfapplicant.feature.auth.dto.SignupRequest
import me.bfapplicant.feature.auth.dto.TokenResponse
import me.bfapplicant.feature.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "인증", description = "회원가입, 로그인, 토큰 갱신, 로그아웃 API")
@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @Operation(
        summary = "회원가입",
        responses = [
            ApiResponse(responseCode = "201", description = "가입 성공"),
            ApiResponse(responseCode = "400", description = "이메일 중복 또는 유효성 검증 실패")
        ]
    )
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody req: SignupRequest): TokenResponse =
        authService.signup(req)

    @Operation(
        summary = "로그인",
        responses = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "400", description = "이메일 또는 비밀번호 불일치")
        ]
    )
    @PostMapping("/login")
    fun login(@Valid @RequestBody req: LoginRequest): TokenResponse =
        authService.login(req)

    @Operation(
        summary = "토큰 갱신",
        description = "Refresh Token을 사용하여 새 토큰 쌍을 발급받습니다. 기존 Refresh Token은 즉시 폐기됩니다 (토큰 회전).",
        responses = [
            ApiResponse(responseCode = "200", description = "갱신 성공"),
            ApiResponse(responseCode = "401", description = "유효하지 않거나 폐기된 Refresh Token")
        ]
    )
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody req: RefreshRequest): TokenResponse =
        authService.refresh(req.refreshToken)

    @Operation(
        summary = "로그아웃",
        description = "해당 사용자의 모든 Refresh Token을 폐기합니다.",
        responses = [ApiResponse(responseCode = "204", description = "로그아웃 성공")]
    )
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@AuthenticationPrincipal userId: UUID) {
        authService.logout(userId)
    }
}
