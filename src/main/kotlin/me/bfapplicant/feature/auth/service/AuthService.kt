package me.bfapplicant.feature.auth.service

import me.bfapplicant.domain.entity.ApplicantUser
import me.bfapplicant.domain.entity.RefreshToken
import me.bfapplicant.domain.repository.ApplicantUserRepository
import me.bfapplicant.domain.repository.RefreshTokenRepository
import me.bfapplicant.feature.auth.dto.LoginRequest
import me.bfapplicant.feature.auth.dto.SignupRequest
import me.bfapplicant.feature.auth.dto.TokenResponse
import me.bfapplicant.infra.security.jwt.JwtProperties
import me.bfapplicant.infra.security.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthService(
    private val userRepository: ApplicantUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtProperties: JwtProperties,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signup(req: SignupRequest): TokenResponse {
        require(userRepository.findByUserEmailContact(req.email) == null) {
            "Email already registered"
        }
        val user = userRepository.save(
            ApplicantUser(
                userName = req.userName,
                userEmailContact = req.email,
                password = passwordEncoder.encode(req.password)!!
            )
        )
        return issueTokenPair(user)
    }

    @Transactional
    fun login(req: LoginRequest): TokenResponse {
        val user = userRepository.findByUserEmailContact(req.email)
            ?: throw IllegalArgumentException("Invalid email or password")
        require(passwordEncoder.matches(req.password, user.password)) {
            "Invalid email or password"
        }
        return issueTokenPair(user)
    }

    @Transactional
    fun refresh(rawRefreshToken: String): TokenResponse {
        require(jwtTokenProvider.validateToken(rawRefreshToken)) {
            "Invalid or expired refresh token"
        }

        val hash = sha256(rawRefreshToken)
        val revokedCount = refreshTokenRepository.revokeByHashIfActive(hash)

        if (revokedCount == 0) {
            // Already revoked or non-existent -> check for theft
            val existing = refreshTokenRepository.findByTokenHash(hash)
            if (existing != null && existing.revoked) {
                val ownerId = existing.user.userId!!
                refreshTokenRepository.revokeAllByUserUserId(ownerId)
            }
            throw IllegalArgumentException("Refresh token is no longer valid")
        }

        val userId = jwtTokenProvider.getUserIdFromToken(rawRefreshToken)
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        return issueTokenPair(user)
    }

    @Transactional
    fun logout(userId: UUID) {
        refreshTokenRepository.revokeAllByUserUserId(userId)
    }

    private fun issueTokenPair(user: ApplicantUser): TokenResponse {
        val id = user.userId!!
        val accessToken = jwtTokenProvider.generateAccessToken(id, user.userEmailContact)
        val refreshToken = jwtTokenProvider.generateRefreshToken(id)

        refreshTokenRepository.save(
            RefreshToken(
                user = user,
                tokenHash = sha256(refreshToken),
                expiresAt = LocalDateTime.now().plusSeconds(jwtProperties.refreshExpirationMs / 1000)
            )
        )

        return TokenResponse(accessToken, refreshToken)
    }

    private fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
