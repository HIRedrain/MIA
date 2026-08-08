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

import mia.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class InstagramCommentService (
    private val postRepository: PostRepository
) {

}