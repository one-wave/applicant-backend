package me.bfapplicant.feature.profile.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.bfapplicant.feature.profile.dto.ChangePasswordRequest
import me.bfapplicant.feature.profile.dto.ProfileRequest
import me.bfapplicant.feature.profile.dto.ProfileResponse
import org.springframework.http.HttpStatus
import me.bfapplicant.feature.profile.service.ProfileService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "프로필", description = "사용자 프로필 조회/수정 API")
@RestController
@RequestMapping("/api/profile")
class ProfileController(private val profileService: ProfileService) {

    @Operation(
        summary = "내 프로필 조회",
        description = "로그인한 사용자의 기본정보 + 신체환경 조건을 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패")
        ]
    )
    @GetMapping
    fun getProfile(@AuthenticationPrincipal userId: UUID): ProfileResponse =
        profileService.getProfile(userId)

    @Operation(
        summary = "내 프로필 수정",
        description = "전화번호, 생년월일, 신체환경 조건 6개 항목을 수정합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "수정 성공"),
            ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
            ApiResponse(responseCode = "401", description = "인증 실패")
        ]
    )
    @PutMapping
    fun updateProfile(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody req: ProfileRequest
    ): ProfileResponse = profileService.updateProfile(userId, req)

    @Operation(
        summary = "비밀번호 변경",
        description = "현재 비밀번호 확인 후 새 비밀번호로 변경합니다.",
        responses = [
            ApiResponse(responseCode = "204", description = "변경 성공"),
            ApiResponse(responseCode = "400", description = "현재 비밀번호 불일치 또는 유효성 검증 실패"),
            ApiResponse(responseCode = "401", description = "인증 실패")
        ]
    )
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody req: ChangePasswordRequest
    ) = profileService.changePassword(userId, req)
}
