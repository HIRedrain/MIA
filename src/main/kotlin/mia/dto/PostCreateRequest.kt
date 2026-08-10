/* PostCreateRequest.kt
* MIA - 인스타그램 자동화
* 인스타그램 게시물 관련 webhook 으로 전달된 원본 데이터 (Json) => Kotlin 객체 변환
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

data class PostCreateRequest(
    val postURL: String,
    val productName: String,
    val productURL: String,
    val keyword: String,
    val dmMessage: String
) {
    override fun toString(): String {
        return "PostCreateRequest (postURL : $postURL, productName = $productName, \n   productURL = $productURL, \n   keyword = $keyword, \n   dmMessage = $dmMessage, "
    }
}
