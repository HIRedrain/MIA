/* UserExtensions.kt
* MIA - 인스타그램 자동화
* User 관련 확장 함수 파일
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     User.toResponse() 작성
* ========================================================
*/


package mia.dto.user

import mia.entity.type.UserType
import java.time.LocalDateTime

data class UserResponse(
    val userId: Int,
    val loginId: String,
    val nickname: String,
    val role: UserType,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime
)
