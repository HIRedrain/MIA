/* PostService.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 이벤트를 실제 데이터로 변환 후 비즈니스 규칙대로 처리
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     Service 생성
* 이홍비    2026.08.10     post crud 처리
* ========================================================
*/


package mia.service

import mia.dto.InstagramMediaResponse
import mia.dto.PostCreateRequest
import mia.entity.Post
import mia.repository.PostRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService (
    private val postRepository: PostRepository) {

    fun createPost(request: PostCreateRequest) {

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



    fun createPostTest(request: PostCreateRequest) {

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