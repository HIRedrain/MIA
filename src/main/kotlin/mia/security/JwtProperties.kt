/* JwtProperties.kt
* MIA - 인스타그램 자동화
* JWT 관련 설정 정보 관리
* application.yml의 jwt 설정 값을 바인딩하여 JWT 생성 및 인증에 필요한 설정 제공
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     JwtProperties 생성
* ========================================================
*/

package mia.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val expiration: Long = 7_200_000L, // 2시간 = 120분 = 7200초 = 720,0000ms
    val cookieName: String = "MIA_ACCESS_TOKEN",
    val secure: Boolean = false
)