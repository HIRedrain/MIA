/* User.kt
* MIA - 인스타그램 자동화
* 사용자 관련 Entity
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     User 생성
* ========================================================
*/

package mia.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import mia.entity.type.UserType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@Table(name = "users")
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val userId: Int? = null,

    @Column(name = "login_id", nullable = false, unique = true, length = 20)
    var loginId: String,

    @Column(name = "login_pw", nullable = false, length = 61)
    var loginPw: String,

    @Column(name = "user_nickname", nullable = false, unique = true, length = 20)
    var userNickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 7)
    var userRole: UserType = UserType.CLIENT,

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    val createdDate: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false, updatable = true)
    val modifiedDate: LocalDateTime? = null
) {

    // 출력 형식 지정
    override fun toString(): String {
        return "User (userId : $userId, loginId : $loginId, userNickname : $userNickname, userRole : $userRole)"
    }

}