/* PostController.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 처리 controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     Controller 생성
* 이홍비    2026.08.10     save => create 로 변경
* ========================================================
*/

package mia.controller

import mia.dto.PostCreateRequest
import mia.entity.Post
import mia.service.PostService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController (
    val postService: PostService
) {

    @PostMapping
    fun createPost(
        @RequestBody request: PostCreateRequest
    ): String {
        postService.createPost(request)

        return ""
    }

    @PostMapping("/test")
    fun createPostTest(
        @RequestBody request: PostCreateRequest
    ): String {
        postService.createPostTest(request)

        return ""
    }

}