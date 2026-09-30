/* LoginController.kt
* MIA - 인스타그램 자동화
* 로그인 관련 Controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     LoginController 작성
* ========================================================
*/


package mia.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(): String = "pages/login"

    @GetMapping("/signup")
    fun signup(): String = "pages/signup"
}
