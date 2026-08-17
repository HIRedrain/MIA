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
* 이홍비    2026.08.17     getMediaFromPostURL() 내부 구현
* ========================================================
*/


package mia.service

import mia.dto.InstagramMediaListResponse
import mia.dto.InstagramMediaResponse
import mia.dto.PostCreateRequest
import mia.entity.Post
import mia.repository.PostRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class PostService (
    private val postRepository: PostRepository,
    private val restClient: RestClient, // 외부 API 와 통신할 때 사용하는 HTTP Client (Spring 6.1 ~)
    @Value("\${meta.instagram.access-token}") // yaml 에 저장된 토큰 값 => accessToken 변수로 저장
    private val accessToken: String
) {

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
        try {
            // 내 인스타그램 계정의 미디어 목록을 조회 (permalink와 timestamp, id를 포함)
            val response = restClient.get()
                .uri("https://graph.instagram.com/v25.0/me/media?fields=id,permalink,timestamp,media_product_type&limit=50&access_token=$accessToken")
                .retrieve()
                .body(InstagramMediaListResponse::class.java)

            // 사용자가 입력한 postURL과 API에서 가져온 permalink가 일치하는 항목 탐색
            val matchedMedia = response?.data?.find { item ->
                item.permalink != null && postURL.contains(item.permalink.removeSuffix("/"))
            }

            return matchedMedia
        } catch (e: Exception) {
            println("❌ 인스타그램 API 호출 중 오류 발생: ${e.message}")
            return null
        }
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