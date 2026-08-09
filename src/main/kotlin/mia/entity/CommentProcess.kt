/* CommentProcess.kt
* MIA - 인스타그램 자동화
* 인스타그램 댓글 처리 관련 entity
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     entity 생성
* ========================================================
*/

package mia.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "comment_process")
class CommentProcess (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cp_id")
    val cpId: Long? = null, // 관리용

    @Column(name = "media_id", nullable = false, length = 100)
    val mediaId: String,

    @Column(name = "comment_id", nullable = false, unique = true, length = 100)
    val commentId: String, // 댓글 고유의 id => 중복 될 일 없음

    @Column(name = "commenter_id", nullable = false, length = 100)
    val commenterId: String,

    @CreationTimestamp
    @Column(name = "processed_date", nullable = false, updatable = false)
    val processedDate: LocalDateTime? = null
) {
    // 출력 형식 지정
    override fun toString(): String {
        return "CommentProcess (cpId : $cpId, mediaId = $mediaId, commentId : $commentId, commenterId = $commenterId, processedDate : $processedDate)"
    }
}