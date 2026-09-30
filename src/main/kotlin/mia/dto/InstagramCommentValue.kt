/* InstagramCommentValue.kt
* MIA - 인스타그램 자동화
* Meta 에게서 받은 JSON 에서의 댓글 핵심  DTO - 4
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.11
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.11     InstagramCommentValue 작성
* ========================================================
*/


package mia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class InstagramCommentValue(
    val from: InstagramCommenter,
    val media: InstagramMedia,
    val id: String,

    @JsonProperty("parent_id") // JSON 에서 사용하는 외부 property 이름 지정하는 Jackson annotation => 자동 주입
    val parentId: String?, // 상위 ID - 게시물에 달린 댓글 => 게시물 ID, 대댓글 => 대댓글 단 그 상위의 댓글 ID 

    val text: String
)
