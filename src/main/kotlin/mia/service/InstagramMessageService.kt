/* InstagramMessageService.kt
* MIA - 인스타그램 자동화
* 인스타그램 DM 발송 관련 Service
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.17
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.17     Service 생성
* ========================================================
*/


package mia.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

class InstagramMessageService (
    private val restClient: RestClient, // 외부 API 와 통신할 때 사용하는 HTTP Client (Spring 6.1 ~)
    @Value("\${meta.instagram.access-token}") // yaml 에 저장된 토큰 값 => accessToken 변수로 저장
    private val accessToken: String
) {
    fun sendMessage(
        recipientId: String, // commenterId - 받는 사람 ID
        message: String
    ) {
        restClient.post() // HTTP POST method 사용 => 요청
            .uri("https://graph.instagram.com/v25.0/me/messages") // 인스타그램 메시지 발송 endpoint
            .header( // Header 에 인증 관련 정보 (access-token) 입력
                HttpHeaders.AUTHORIZATION,
                "Bearer $accessToken"
            )
            .contentType(MediaType.APPLICATION_JSON) // 서버로 전송하는 데이터 형식 : JSON
            .body( // 인스타그램 API 규격에 맞는 Payload - Map 형태로 구성
                mapOf(
                    "recipient" to mapOf(
                        "id" to recipientId // 받을 사람 ID
                    ),
                    "message" to mapOf(
                        "text" to message // 메시지 내용
                    )
                )
            )
            .retrieve() // 요청 실행
            .toBodilessEntity() // 응답 본문 Body 따로 안 받음 - 성공 여부만 확인 - HTTP 요청 완료

        println("✅ DM 발송 성공")
    }
}