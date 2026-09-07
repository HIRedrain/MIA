/* PostResponse.kt
* MIA - 인스타그램 자동화
* PostResponse DTO
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.05
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.05     PostResponse 작성
* ========================================================
*/


package mia.dto

import java.time.LocalDateTime

data class PostResponse(
    val postId: Long,
    val mediaId: String,
    val instagramUrl: String,
    val imageUrl: String?,
    val productName: String,
    val productUrl: String,
    val keyword: String,
    val dmMessage: String,
    val instagramCreatedDate: LocalDateTime,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime
    )
