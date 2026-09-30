/* AdminUserExceptionHandler.kt
* MIA - 인스타그램 자동화
* 관리자 쪽 회원 관리 관련 예외 처리 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.10.01
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.10.01     AdminUserExceptionHandler 작성
* ========================================================
*/


package mia.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AdminUserExceptionHandler {

    @ExceptionHandler(AdminUserException::class)
    fun handleAdminUserException(
        e: AdminUserException
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to (e.message ?: "회원 관리 요청을 처리하지 못했습니다.")))
    }
}
