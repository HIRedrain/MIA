/* GlobalExceptionHandler.kt
* MIA - 인스타그램 자동화
* 전역 예외 처리 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.07
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.07     GlobalExceptionHandler 생성
* 이홍비    2026.09.09     패키지 이름 변경 (common => exception)
* 이홍비    2026.10.01     handleNoResourceFoundException() 추가
* 이홍비    2026.10.01     handleException() 추가
* ========================================================
*/

package mia.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException
    ): ResponseEntity<Map<String, String?>> {

        return ResponseEntity
            .badRequest()
            .body(
                mapOf(
                    "message" to e.message
                )
            )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(
        e: NoResourceFoundException
    ): ResponseEntity<Map<String, String>> {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                mapOf(
                    "message" to "요청한 리소스를 찾을 수 없습니다."
                )
            )
    }


    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception
    ): ResponseEntity<Map<String, String>> {

        e.printStackTrace()

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                mapOf(
                    "message" to "요청 처리 중 오류가 발생했습니다."
                )
            )
    }
}