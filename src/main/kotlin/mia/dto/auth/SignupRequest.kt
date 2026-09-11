/* SignupRequest.kt
* MIA - 인스타그램 자동화
* 회원 가입 관련 DTO
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     SignupRequest 작성
* ========================================================
*/


package mia.dto.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(

    @field:NotBlank(message = "아이디를 입력해 주세요.")
    @field:Size(max = 20, message = "아이디는 20자 이하로 입력해 주세요.")
    val loginId: String,

    @field:NotBlank(message = "비밀번호를 입력해 주세요.")
    @field:Size(
        min = 5,
        max = 50,
        message = "비밀번호는 5자 이상 50자 이하로 입력해 주세요."
    )
    val loginPw: String,

    @field:NotBlank(message = "닉네임을 입력해 주세요.")
    @field:Size(max = 20, message = "닉네임은 20자 이하로 입력해 주세요.")
    val userNickname: String
)
