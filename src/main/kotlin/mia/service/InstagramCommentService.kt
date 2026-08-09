/* InstagramCommentService.kt
* MIA - 인스타그램 자동화
* 인스타그램 댓글 관련 이벤트를 실제 데이터로 변환 후 비즈니스 규칙대로 처리
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     Service 생성
* ========================================================
*/


package mia.service

import mia.dto.CommentWebhookRequest
import mia.entity.CommentProcess
import mia.repository.CommentProcessRepository
import mia.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class InstagramCommentService (
    private val postRepository: PostRepository,
    private val commentProcessRepository: CommentProcessRepository
) {
    fun process(request: CommentWebhookRequest) {

        if (commentProcessRepository.existsByCommentId(request.commentId)) {
            // 이미 처리한 댓글
            println("❗ 이미 처리한 댓글 ❗")
            return
        }

        val post = postRepository.findByMediaId(request.mediaId)  // ?: return // null 이 아니면 post 객체를 변수에 저장하고, null 이면 return (함수 종료)
        if (post == null) {
            // 존재하지 않는 게시물
            println("❌ 존재하지 않는 게시물 ❌")
            return
        }


        val normalizedComment = normalize(request.commentText)
        val normalizedKeyword = normalize(post.keyword)
        //if (request.commentText != post.keyword) {
        if (!normalizedComment.contains(normalizedKeyword)) { // 단어 포함 여부 확인
            println("❌ 핵심 단어 (${post.keyword}) 포함 x ❌")
            return
        }


        println("✅ Post URL : ${post.productUrl}")
        println("✅ Keyword : ${post.keyword}")
        println("✅ DM Text : ${request.commentText}")
        println("✅ DM 수신 대상 : ${request.commenterId}")


        // 처리한 댓글 정보 저장 과정
        val commentProcess = CommentProcess(
            mediaId = request.mediaId,
            commentId = request.commentId,
            commenterId = request.commenterId
        )
        println("✅ 댓글 처리 저장 : ${commentProcessRepository.save(commentProcess)}")
    }

    private fun normalize(text: String): String {
        return text
            .replace("\\s+".toRegex(), "") // 모든 종류의 공백(스페이스, 탭, 줄바꿈 등)이 한 글자 이상 연속된 패턴 => "" 로 변경 (공란 x)
            .trim() // 맨 앞, 맨 뒤 공백 제거 (혹시 모를 확인용)
    }



}