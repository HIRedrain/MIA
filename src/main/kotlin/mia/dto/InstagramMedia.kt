/* InstagramMedia.kt
* MIA - 인스타그램 자동화
* Meta 에게서 받은 JSON 에서의 게시물 정보 DTO - 6
* Webhook 받을 때 씀
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.11
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.11     InstagramMedia 작성
* 이홍비    2026.08.12     mediaProductType null 가능 처리
* ========================================================
*/

package mia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class InstagramMedia(
    val id: String,

    @JsonProperty("media_product_type")
    val mediaProductType: String? = null // 예 : FEED
)
