/* InstagramWebhookEntry.kt
* MIA - 인스타그램 자동화
* Meta 에게서 받은 JSON 에서의 Entry DTO - 2
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.11
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.11     InstagramWebhookEntry 작성
* ========================================================
*/



package mia.dto

data class InstagramWebhookEntry(
    val id: String,
    val time: Long, // Json 에서 숫자로 넘어옴
    val changes: List<InstagramWebhookChange> // 여러 개 넘어올 수 있어서
)
