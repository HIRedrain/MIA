/* AdminPostRestController.kt
* MIA - 인스타그램 자동화
* 관리자 - 인스타그램 게시물 관련 처리 rest controller
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
* 이홍비    2026.08.10     crud
* 이홍비    2026.08.20     rest controller 로 변경
* 이홍비    2026.09.06     Pageing 처리
* 이홍비    2026.09.07     Mapping 처리 보완
* 이홍비    2026.09.07     api 변경, 클래스명 변경
* ========================================================
*/

package mia.controller

import mia.dto.PostCreateRequest
import mia.dto.PostResponse
import mia.dto.PostUpdateRequest
import mia.service.PostService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/posts")
class AdminPostRestController (
    val postService: PostService
) {

    @GetMapping
    fun getPosts(
        @RequestParam(defaultValue = "0") page: Int
    ): ResponseEntity<Page<PostResponse>> {
        return ResponseEntity.ok(postService.getPosts(page))
    }

    @GetMapping("/{pid}")
    fun getPost(
        @PathVariable("pid") pid: Long
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.getPost(pid))
    }

    @PostMapping
    fun createPost(
        @RequestBody request: PostCreateRequest
    ): ResponseEntity<PostResponse> {

        val post = postService.createPost(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(post) // 201
    }

//    @PostMapping("/test")
//    fun createPostTest(
//        @RequestBody request: PostCreateRequest
//    ): String {
//        postService.createPostTest(request)
//
//        return ""
//    }

    @PutMapping("/{pid}")
    fun updatePost(
        @PathVariable("pid") pid: Long,
        @RequestBody request: PostUpdateRequest
    ): ResponseEntity<PostResponse> {

        return ResponseEntity.ok(postService.updatePost(pid, request)) // 200
    }

    @DeleteMapping("/{pid}")
    fun deletePost(
        @PathVariable("pid") pid: Long
    ): ResponseEntity<Void> {
        postService.deletePost(pid)

        return ResponseEntity(HttpStatus.NO_CONTENT) // 204
    }




}