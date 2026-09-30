/* InstagramWebhookRequest.kt
* MIA - 인스타그램 자동화
* Meta 에게서 받은 JSON 에서의 최상위 DTO - 1
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.11
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.11     InstagramWebhookRequest 작성
* ========================================================
*/


package mia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class InstagramWebhookRequest(
    val entry: List<InstagramWebhookEntry>, // 여러 개 넘어올 수 있어서

    // object - 예 : 'instagram'
    // 키워드 (object 등) 이름으로 쓰는 방법
    // 1. `` (백틱) : 코틀린에 존재하는 키워드를 식별자로 쓰고 싶을 때 백틱으로 감싸서 쓰면 됨 - 참고로 함수명에 띄어 쓰기 하고 싶을 때도 씀
    // val `object`: String

    // 2.
    @JsonProperty("object")
    val objectType: String
)

