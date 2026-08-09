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
