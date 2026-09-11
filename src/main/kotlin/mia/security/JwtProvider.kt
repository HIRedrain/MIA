/* JwtProvider.kt
* MIA - 인스타그램 자동화
* 토근 생성, 암호화 관련 내용
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     JwtProvider 생성
* ========================================================
*/

package mia.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mia.entity.User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties
) {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(
            jwtProperties.secret.toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun createToken(user: User): String {
        val now = Date()
        val expiresAt = Date(now.time + jwtProperties.expiration)

        return Jwts.builder()
            .subject(user.loginId)
            .claim("userId", user.userId)
            .claim("nickname", user.userNickname)
            .claim("role", user.userRole.name)
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(signingKey)
            .compact()
    }

    fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun isValid(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (_: Exception) {
            false
        }
    }
}
