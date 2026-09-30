/* AuthService.kt
* MIA - 인스타그램 자동화
* 인증 관련 Service
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     AuthService 생성
* ========================================================
*/

package mia.service

import mia.dto.auth.LoginRequest
import mia.entity.User
import mia.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): User {

        // 회원가입과 동일하게 아이디를 정규화해서 조회
        val loginId = request.loginId
            .trim()
            .lowercase()

        val user = userRepository.findByLoginId(loginId)
            ?: throw BadCredentialsException(
                "아이디 또는 비밀번호가 올바르지 않습니다."
            )

        if (!passwordEncoder.matches(request.loginPw, user.loginPw)) {
            throw BadCredentialsException(
                "아이디 또는 비밀번호가 올바르지 않습니다."
            )
        }

        return user
    }
}
