/* InstagramWebhookChange.kt
* MIA - 인스타그램 자동화
* Meta 에게서 받은 JSON 에서의 changes DTO - 3
* 게시물 상태 변경, 댓글 변경 등...
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.11
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.11     InstagramWebhookChange 작성
* ========================================================
*/


package mia.dto

data class InstagramWebhookChange(
    val field: String, // 예 : Comments
    val value: InstagramCommentValue
)
