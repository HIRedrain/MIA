/* UserExceptionHandler.kt
* MIA - 인스타그램 자동화
* 사용자 관련 예외 처리 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     UserExceptionHandler 생성
* ========================================================
*/


package mia.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun alreadyExists(
        e: UserAlreadyExistsException
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(mapOf("message" to (e.message ?: "이미 사용 중인 회원 정보입니다.")))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(
        e: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, String>> {
        val message = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
            ?: "입력값을 확인해 주세요."

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to message))
    }
}
