/* Post.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 entity
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
import java.time.LocalDateTime


@Entity
@Table(name = "post")
class Post (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val postId: Long? = null, // 관리용

    @Column(name = "media_id", nullable = false, unique = true)
    val mediaId: Long, // 인스타그램 - 게시물 id

    @Column(name = "product_name", nullable = false, length = 100)
    val productName: String,

    @Column(name = "product_url", nullable = false, columnDefinition = "TEXT")
    val productUrl: String,

    @Column(name = "keyword", nullable = false, length = 30)
    val keyword: String,

    @Column(name = "dm_message", nullable = false, columnDefinition = "TEXT")
    val dmMessage: String,

    @Column(name = "created_date", nullable = false)
    val createdDate: LocalDateTime? = null,

    @Column(name = "modified_date", nullable = false)
    val modifiedDate: LocalDateTime? = null
) {

}