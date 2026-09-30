/* PostRepository.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 entity 의 Repository
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     Repository 생성
* 이홍비    2026.09.07     existsByMediaId() 추가
* ========================================================
*/


package mia.repository

import mia.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<Post, Long> {

    fun findByMediaId(mediaId: String): Post? // mediaId 로 Post get
    fun existsByMediaId(mediaId: String): Boolean // mediaId 가 존재하는가
}