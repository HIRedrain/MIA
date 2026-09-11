/* JwtAuthenticationFilter.kt
* MIA - 인스타그램 자동화
* 토큰 처리용 필터
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     JwtAuthenticationFilter 생성
* ========================================================
*/


package mia.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val jwtProperties: JwtProperties
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.cookies
            ?.firstOrNull { it.name == jwtProperties.cookieName }
            ?.value

        if (
            !token.isNullOrBlank() &&
            SecurityContextHolder.getContext().authentication == null &&
            jwtProvider.isValid(token)
        ) {
            val claims = jwtProvider.parseClaims(token)
            val loginId = claims.subject
            val role = claims["role"] as? String

            if (!loginId.isNullOrBlank() && !role.isNullOrBlank()) {
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(
                        loginId,
                        null,
                        listOf(SimpleGrantedAuthority("ROLE_$role"))
                    )
            }
        }

        filterChain.doFilter(request, response)
    }
}
