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
    fun savePost(
        @RequestBody request: PostCreateRequest
    ): String {
        postService.savePost(request)

        return ""
    }

    @PostMapping("/test")
    fun savePostTest(
        @RequestBody request: PostCreateRequest
    ): String {
        postService.savePostTest(request)

        return ""
    }

}