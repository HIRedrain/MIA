/* AdminUserService.kt
* MIA - 인스타그램 자동화
* 관리자 쪽 회원 관리 관련 Service 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.10.01
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.10.01     AdminUserService 생성
* ========================================================
*/



package mia.service

import mia.dto.PageResponse
import mia.dto.admin.user.AdminUserResponse
import mia.entity.type.UserType
import mia.exception.AdminUserException
import mia.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminUserService(
    private val userRepository: UserRepository
) {

    fun getUsers(page: Int, size: Int): PageResponse<AdminUserResponse> {
        val pageable = PageRequest.of(
            page.coerceAtLeast(0),
            size.coerceIn(1, 100),
            Sort.by(Sort.Direction.ASC, "userId")
        )

        val users = userRepository.findAll(pageable)

        return PageResponse(
            content = users.content.map(AdminUserResponse::from),
            page = users.number,
            size = users.size,
            totalElements = users.totalElements,
            totalPages = users.totalPages,
            first = users.isFirst,
            last = users.isLast
        )
    }

    fun getUser(userId: Int): AdminUserResponse {
        return AdminUserResponse.from(findUser(userId))
    }

    @Transactional
    fun updateRole(
        currentLoginId: String,
        userId: Int,
        role: UserType
    ): AdminUserResponse {
        val user = findUser(userId)

        println("✅ 기존 user : $user")


        if (user.loginId == currentLoginId) {
            throw AdminUserException("현재 로그인한 계정의 권한은 변경할 수 없습니다.")
        }

        if (user.userRole == role) {
            return AdminUserResponse.from(user)
        }

        user.userRole = role

        println("✅ user role 변경 : $user")


        return AdminUserResponse.from(user)
    }

    @Transactional
    fun deleteUser(
        currentLoginId: String,
        userId: Int
    ) {
        val user = findUser(userId)

        if (user.loginId == currentLoginId) {
            throw AdminUserException("현재 로그인한 계정은 강제 탈퇴시킬 수 없습니다.")
        }

        println("✅ user 삭제 : ${userRepository.delete(user)}")
    }

    private fun findUser(userId: Int) =
        userRepository.findById(userId)
            .orElseThrow { AdminUserException("존재하지 않는 회원입니다.") }
}
