/* UserService.kt
* MIA - 인스타그램 자동화
* 사용자 관련 Service
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     UserService 생성
* ========================================================
*/

package mia.service

import mia.dto.auth.SignupRequest
import mia.entity.User
import mia.exception.UserAlreadyExistsException
import mia.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createUser(request: SignupRequest) {

        // 영문 아이디는 항상 소문자로 저장
        // 한글 아이디는 그대로 유지
        val loginId = request.loginId
            .trim()
            .lowercase()

        val nickname = request.userNickname.trim()

        if (userRepository.existsByLoginId(loginId)) {
            throw UserAlreadyExistsException("이미 사용 중인 아이디입니다.")
        }

        if (userRepository.existsByUserNickname(nickname)) {
            throw UserAlreadyExistsException("이미 사용 중인 닉네임입니다.")
        }

        val encodedPassword = requireNotNull(
            passwordEncoder.encode(request.loginPw)
        )

        val user = User(
            loginId = loginId,
            loginPw = encodedPassword,
            userNickname = nickname
        )

        userRepository.save(user)
    }
}
