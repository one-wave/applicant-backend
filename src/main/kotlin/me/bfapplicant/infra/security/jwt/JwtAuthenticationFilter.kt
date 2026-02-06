package me.bfapplicant.infra.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.bfapplicant.feature.auth.service.AuthService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authService: AuthService
) : OncePerRequestFilter() {

    companion object {
        private const val REFRESH_HEADER = "X-Refresh-Token"
        private const val NEW_ACCESS_HEADER = "X-New-Access-Token"
        private const val NEW_REFRESH_HEADER = "X-New-Refresh-Token"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        extractToken(request)?.let { accessToken ->
            when {
                jwtTokenProvider.validateToken(accessToken) -> {
                    setAuthentication(accessToken)
                }
                jwtTokenProvider.isTokenExpired(accessToken) -> {
                    request.getHeader(REFRESH_HEADER)?.let { refreshToken ->
                        tryAutoRefresh(refreshToken, response)
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun tryAutoRefresh(refreshToken: String, response: HttpServletResponse) {
        try {
            val tokens = authService.refresh(refreshToken)

            setAuthentication(tokens.accessToken)

            response.setHeader(NEW_ACCESS_HEADER, tokens.accessToken)
            response.setHeader(NEW_REFRESH_HEADER, tokens.refreshToken)
        } catch (_: Exception) {
        }
    }

    private fun setAuthentication(token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                userId, null, listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
    }

    private fun extractToken(request: HttpServletRequest): String? =
        request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
}
