/* PageResponse.kt
* MIA - 인스타그램 자동화
* Page 응답 관련 DTO - PageImpl 경고 제거용
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.10.01
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.10.01     PageResponse 작성
* ========================================================
*/


package mia.dto

data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean
)