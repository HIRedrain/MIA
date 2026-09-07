/* AdminPageController.kt
* MIA - 인스타그램 자동화
* 관리자 페이지 쪽 controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.20
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.20     AdminPageController 작성
* ========================================================
*/


package mia.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminPageController {

    @GetMapping
    fun adminHome(): String {
        return "pages/admin/admin"
    }


    @GetMapping("/posts")
    fun posts(): String {
        return "pages/admin/posts"
    }

    @GetMapping("/posts/{pid}")
    fun postDetails(
        @PathVariable pid: Long
    ): String {
        return "pages/admin/post-detail"
    }

    @GetMapping("/posts/create")
    fun postCreate(): String {
        return "pages/admin/post-create"
    }

    @GetMapping("/posts/{pid}/update")
    fun postUpdate(
        @PathVariable pid: Long
    ): String {
        return "pages/admin/post-update"
    }


}