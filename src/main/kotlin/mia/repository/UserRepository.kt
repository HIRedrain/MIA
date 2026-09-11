/* UserRepository.kt
* MIA - 인스타그램 자동화
* 사용자 관련 entity 의 Repository
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     Repository 생성
* ========================================================
*/


package mia.repository

import mia.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByLoginId(loginId: String): User? // 아이디 찾기
    fun existsByLoginId(loginId: String): Boolean // 아이디 존재 여부
    fun existsByUserNickname(userNickname: String): Boolean // 별명 존재 여부
}
