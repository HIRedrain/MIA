package mia.service

import mia.dto.InstagramMediaResponse
import mia.dto.PostCreateRequest
import mia.entity.Post
import mia.repository.PostRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PostService (
    private val postRepository: PostRepository) {

    fun savePost(request: PostCreateRequest) {

        val media = getMediaFromPostURL(request.postURL)
        if (media == null) {
            println("❌ 존재하지 않는 게시물")
            return
        }

        val post = Post(
            mediaId = media.id,
            productName = request.productName,
            productUrl = request.productURL,
            keyword = request.keyword,
            dmMessage = request.dmMessage,
            instagramCreatedDate = media.instagramCreatedDate
        )

        println("✅ post 저장 : ${postRepository.save(post)}")
    }

    private fun getMediaFromPostURL(postURL: String): InstagramMediaResponse? {
        return null
    }



    fun savePostTest(request: PostCreateRequest) {

        println("✅ $request")

        val post = Post(
            mediaId = "137",
            productName = request.productName,
            productUrl = request.productURL,
            keyword = request.keyword,
            dmMessage = request.dmMessage,
            instagramCreatedDate = LocalDateTime.now()
        )

        println("✅ post 저장 : ${postRepository.save(post)}")
    }

}