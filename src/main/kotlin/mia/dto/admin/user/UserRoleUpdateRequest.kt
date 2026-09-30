/* UserRoleUpdateRequest.kt
* MIA - 인스타그램 자동화
* 관리자 회원 권한 변경 요청 DTO
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.10.01
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.10.01     UserRoleUpdateRequest 작성
* ========================================================
*/


package mia.dto.admin.user

import mia.entity.type.UserType


data class UserRoleUpdateRequest(
    val role: UserType
)
