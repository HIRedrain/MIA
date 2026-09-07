/* PostExtensions.kt
* MIA - 인스타그램 자동화
* Post 관련 확장 함수 파일
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.05
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.05     Post.toResponse() 작성
* ========================================================
*/

package mia.extension

import mia.dto.PostResponse
import mia.entity.Post

fun Post.toResponse(): PostResponse =
    PostResponse(
        postId = postId!!,
        mediaId = mediaId,
        instagramUrl = instagramUrl,
        imageUrl = imageUrl,
        productName = productName,
        productUrl = productUrl,
        keyword = keyword,
        dmMessage = dmMessage,
        instagramCreatedDate = instagramCreatedDate,
        createdDate = createdDate!!,
        modifiedDate = modifiedDate!!
    ) // !! ; Not-null assertion operator - 해당 값 null x 컴파일러에게 강제 선언