/* UserRestController.kt
* MIA - 인스타그램 자동화
* User 관련 Rest Controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     UserRestController 작성
* ========================================================
*/

package mia.controller

import jakarta.validation.Valid
import mia.dto.auth.SignupRequest
import mia.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserRestController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(
        @Valid @RequestBody request: SignupRequest
    ): ResponseEntity<Void> {

        userService.createUser(request)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }


}