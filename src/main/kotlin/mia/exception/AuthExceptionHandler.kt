/* AuthExceptionHandler.kt
* MIA - 인스타그램 자동화
* 인증 예외 처리 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     AuthExceptionHandler 생성
* ========================================================
*/

package mia.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentials(
        e: BadCredentialsException
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(mapOf("message" to (e.message ?: "로그인에 실패했습니다.")))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDenied(
        e: AccessDeniedException
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(mapOf("message" to (e.message ?: "접근 권한이 없습니다.")))
    }
}
