package me.bfapplicant.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

@Component
class JwtTokenProvider(private val props: JwtProperties) {

    fun generateAccessToken(userId: UUID, email: String): String =
        buildToken(userId, email, props.accessExpirationMs)

    fun generateRefreshToken(userId: UUID): String =
        buildToken(userId, null, props.refreshExpirationMs)

    fun validateToken(token: String): Boolean = try {
        parseClaims(token)
        true
    } catch (_: Exception) {
        false
    }

    fun getUserIdFromToken(token: String): UUID =
        UUID.fromString(parseClaims(token).subject)

    private fun buildToken(userId: UUID, email: String?, expirationMs: Long): String {
        val now = Date()
        val builder = Jwts.builder()
            .subject(userId.toString())
            .id(UUID.randomUUID().toString())
            .issuedAt(now)
            .expiration(Date(now.time + expirationMs))
            .signWith(props.rsaPrivateKey, Jwts.SIG.RS256)

        if (email != null) builder.claim("email", email)

        return builder.compact()
    }

    private fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(props.rsaPublicKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
