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
* 이홍비    2026.08.21     RUD 구현
* 이홍비    2026.09.06     전반적인 함수 구현 (반환 관련 처리 등)
* 이홍비    2026.09.06     Paging 기법 처리
* 이홍비    2026.09.07     이미지 관련 처리
* 이홍비    2026.10.01     이미지 url 갱신 관련 처리
* 이홍비    2026.10.01     PageImpl 경고 관련 처리
* 이홍비    2026.10.03     릴스 관련 주소 처리 (~/reel/~ 이 아닌 ~/p/~로 입력해도 가능하게)
* ========================================================
*/


package mia.service

import mia.dto.InstagramMediaListResponse
import mia.dto.InstagramMediaResponse
import mia.dto.PageResponse
import mia.dto.admin.post.PostCreateRequest
import mia.dto.admin.post.PostResponse
import mia.dto.admin.post.PostUpdateRequest
import mia.entity.Post
import mia.extension.toResponse
import mia.repository.PostRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class PostService (
    private val postRepository: PostRepository,
    private val restClient: RestClient, // 외부 API 와 통신할 때 사용하는 HTTP Client (Spring 6.1 ~)
    @Value("\${meta.instagram.access-token}") // yaml 에 저장된 토큰 값 => accessToken 변수로 저장
    private val accessToken: String
) {

    @Transactional
    fun createPost(request: PostCreateRequest): PostResponse {

        val media = getMediaFromPostURL(request.postURL)
            ?: throw IllegalArgumentException("존재하지 않는 Instagram 게시물입니다.")

        if (postRepository.existsByMediaId(media.id)) {
            throw IllegalArgumentException("이미 등록된 인스타그램 게시물 주소입니다.")
        }



        val post = Post(
            mediaId = media.id,
            instagramUrl = media.permalink ?: request.postURL,
            imageUrl = media.imageUrl,
            productName = request.productName,
            productUrl = request.productURL,
            keyword = request.keyword,
            dmMessage = request.dmMessage,
            instagramCreatedDate = media.instagramCreatedDate
        )

        println("✅ post 저장 : $post")

        return postRepository.save(post).toResponse()
    }

    private fun getMediaFromPostURL(postURL: String): InstagramMediaResponse? {
        try {
            // 내 인스타그램 계정의 미디어 목록을 조회 (permalink와 timestamp, id를 포함)
            val response = restClient.get()
                .uri("https://graph.instagram.com/v25.0/me/media?fields=id,permalink,timestamp,media_product_type,media_type,media_url,thumbnail_url&limit=50&access_token=$accessToken")
                .retrieve()
                .body(InstagramMediaListResponse::class.java)

            // postURL 의 핵심 식별 부분 추출
            val inputShortcode = extractInstagramShortcode(postURL)

            println("------------------------------------------")
            println("\uD83D\uDD34 postURL = $postURL")
            println("\uD83D\uDD34 inputShortcode = $inputShortcode")
            println("------------------------------------------")

            // 사용자가 입력한 postURL과 API에서 가져온 permalink가 일치하는 항목 탐색
            val matchedMedia = response?.data?.find { item ->
                val mediaShortcode = item.permalink
                    ?.let { extractInstagramShortcode(it) }

                println("\uD83D\uDFE1 mediaId          = ${item.id}")
                println("\uD83D\uDFE1 permalink        = [${item.permalink}]")
                println("\uD83D\uDFE1 mediaType        = ${item.mediaType}")
                println("\uD83D\uDFE1 thumbnailUrl     = ${item.thumbnailUrl}")
                println("\uD83D\uDFE1 mediaShortcode = $mediaShortcode")
                println("------------------------------------------")

                // 만약 셋 다 true => find ; "찾았음!" => 해당 item 반환
                (inputShortcode != null) && (mediaShortcode != null) && (inputShortcode == mediaShortcode)
            }

            return matchedMedia
        } catch (e: Exception) {
            println("❌ 인스타그램 API 호출 중 오류 발생: ${e.message}")
            return null
        }
    }

    // instagram url 의 핵심 식별 부분 추출 ; /p/~ 여기서 ~ 부분이 핵심임
    // https://www.instagram.com/p/ABCD
    // https://www.instagram.com/reel/ABCD
    // => ABCD 가 핵심 => 해당 부분 추출
    // => 이걸로 getMediaFromPostURL() 에서 동일한 게시물인지 조회할 것
    private fun extractInstagramShortcode(url: String): String? {
        val regex = Regex("""instagram\.com/(?:p|reel|reels)/([^/?#]+)""")

        return regex.find(url)?.groupValues?.get(1)
    }

    @Transactional
    fun getPosts(page: Int): PageResponse<PostResponse> {
        val pageable = PageRequest.of(
            page,
            10,
            Sort.by(Sort.Direction.DESC, "instagramCreatedDate")
        )

        val posts = postRepository.findAll(pageable)

        posts.content.forEach { post ->
            refreshImageUrl(post)
        }

        return PageResponse(
            content = posts.content.map { it.toResponse() },
            page = posts.number,
            size = posts.size,
            totalElements = posts.totalElements,
            totalPages = posts.totalPages,
            first = posts.isFirst,
            last = posts.isLast
        )
    }

    @Transactional
    fun getPost(pid: Long): PostResponse {

        val post = postRepository.findById(pid)
            .orElseThrow {
                IllegalArgumentException("존재하지 않는 Instagram 게시물입니다.")
            }

        refreshImageUrl(post)

        return post.toResponse()
    }

    @Transactional
    fun updatePost(pid: Long, request: PostUpdateRequest): PostResponse {
        val post = postRepository.findById(pid)
            .orElseThrow { IllegalArgumentException("존재하지 않는 Instagram 게시물입니다.") }

        post.productName = request.productName
        post.productUrl = request.productURL
        post.keyword = request.keyword
        post.dmMessage = request.dmMessage

        val updatePost = postRepository.save(post) // 명시

        println("✅ post 갱신 : $updatePost")

        return updatePost.toResponse()
    }

    @Transactional
    fun deletePost(pid: Long) {
        val post = postRepository.findById(pid)
            .orElseThrow { IllegalArgumentException("존재하지 않는 Instagram 게시물입니다.") }

        println("✅ post 삭제 : ${postRepository.delete(post)}")
    }




    private fun getMediaById(mediaId: String): InstagramMediaResponse? {
        return try {
            restClient.get()
                .uri(
                    "https://graph.instagram.com/v25.0/$mediaId" +
                            "?fields=id,permalink,timestamp,media_product_type,media_type,media_url,thumbnail_url" +
                            "&access_token=$accessToken"
                )
                .retrieve()
                .body(InstagramMediaResponse::class.java)
        } catch (e: Exception) {
            println("❌ Instagram 미디어 조회 실패 ($mediaId) : ${e.message}")
            null
        }
    }


    private fun refreshImageUrl(post: Post) {

        val lastUpdated = post.imageUrlUpdatedDate

        // 이미지 URL을 갱신한 지 24시간이 지나지 않았다면
        // Instagram API를 호출하지 않음
        if ((post.imageUrl != null) && (lastUpdated != null) && (lastUpdated.isAfter(LocalDateTime.now().minusHours(24)))
        ) {
            println("⏭\uFE0F Instagram 이미지 URL 갱신 생략 (캐시 유효) : \${post.mediaId}")

            return
        }

        val media = getMediaById(post.mediaId) ?: return
        val latestImageUrl = media.imageUrl ?: return

        post.imageUrl = latestImageUrl
        post.imageUrlUpdatedDate = LocalDateTime.now()

        println("🖼️ Instagram 이미지 URL 갱신 : ${post.mediaId}")
    }
}