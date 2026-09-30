/* AdminUserResponse.kt
* MIA - 인스타그램 자동화
* 관리자 쪽 회원 목록, 상세 화면에서 사용하는 회원 정보 응답 DTO
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.10.01
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.10.01     AdminUserResponse 작성
* ========================================================
*/


package mia.dto.admin.user

import mia.entity.User


data class AdminUserResponse(
    val userId: Int,
    val loginId: String,
    val nickname: String,
    val role: String
) {
    companion object {
        fun from(user: User): AdminUserResponse = AdminUserResponse(
            userId = requireNotNull(user.userId),
            loginId = user.loginId,
            nickname = user.userNickname,
            role = user.userRole.name
        )
    }
}
