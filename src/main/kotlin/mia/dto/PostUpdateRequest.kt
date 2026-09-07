/* PostUpdateRequest.kt
* MIA - 인스타그램 자동화
* PostUpdateRequest DTO
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.05
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.05     PostUpdateRequest 작성
* ========================================================
*/


package mia.dto

data class PostUpdateRequest(
    val productName: String,
    val productURL: String,
    val keyword: String,
    val dmMessage: String
) {
}
