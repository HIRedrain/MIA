/* InstagramMediaResponse.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 데이터 클래스
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     dto 생성
* ========================================================
*/


package mia.dto

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

data class InstagramMediaResponse(
    val id: String,
    val timestamp: String
) {

    // 한국 시간(LocalDateTime)으로 가져오는 Custom Getter
    val instagramCreatedDate: LocalDateTime // 문자열 => 날짜 객체로 읽음
        get() = OffsetDateTime.parse(timestamp) // 시차 정보 파악 (세계 표준시 기준인 걸 아는)
            .atZoneSameInstant(ZoneId.of("Asia/Seoul")) // 한국 표준시로 변환 => 내부적으로 UTC + 9시간 => 한국 시간으로 보정
            .toLocalDateTime() // 순수 날짜와 시간만 남김

}
