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


package mia.extension

import mia.dto.user.UserResponse
import mia.entity.User

fun User.toResponse(): UserResponse =
    UserResponse(
        userId = userId!!,
        loginId = loginId,
        nickname = userNickname,
        role = userRole,
        createdDate = createdDate!!,
        modifiedDate = modifiedDate!!
    )