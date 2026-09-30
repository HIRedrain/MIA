/* AuthRestController.kt
* MIA - 인스타그램 자동화
* 인증 관련 Rest Controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     AuthRestController 작성
* ========================================================
*/

package mia.controller

import mia.security.JwtProperties
import mia.dto.auth.AuthMeResponse
import mia.dto.auth.LoginRequest
import mia.dto.auth.LoginResponse
import mia.repository.UserRepository
import mia.security.JwtProvider
import mia.service.AuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/api/auth")
class AuthRestController(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val jwtProperties: JwtProperties
) {

    @GetMapping("/csrf")
    fun csrf(csrfToken: CsrfToken): Map<String, String> {
        return mapOf(
            "headerName" to csrfToken.headerName,
            "token" to csrfToken.token
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        val user = authService.login(request)
        val token = jwtProvider.createToken(user)

        val cookie = ResponseCookie
            .from(jwtProperties.cookieName, token)
            .httpOnly(true)
            .secure(jwtProperties.secure)
            .sameSite("Strict")
            .path("/")
            .maxAge(Duration.ofMillis(jwtProperties.expiration))
            .build()

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(
                LoginResponse(
                    loginId = user.loginId,
                    nickname = user.userNickname,
                    role = user.userRole.name
                )
            )
    }

    @GetMapping("/me")
    fun me(authentication: Authentication): ResponseEntity<AuthMeResponse> {
        val user = userRepository.findByLoginId(authentication.name)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(
            AuthMeResponse(
                loginId = user.loginId,
                nickname = user.userNickname,
                role = user.userRole.name
            )
        )
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        val cookie = ResponseCookie
            .from(jwtProperties.cookieName, "")
            .httpOnly(true)
            .secure(jwtProperties.secure)
            .sameSite("Strict")
            .path("/")
            .maxAge(Duration.ZERO)
            .build()

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build()
    }
}
