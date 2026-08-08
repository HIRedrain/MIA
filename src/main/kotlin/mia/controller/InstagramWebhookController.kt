/* InstagramWebhookController.kt
* MIA - 인스타그램 자동화
* 인스타그램 이벤트 수신 처리 controller
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.09     controller 생성
* ========================================================
*/

package mia.controller

import mia.service.InstagramCommentService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/webhook")
class InstagramWebhookController (
    private val commentService: InstagramCommentService
) {
}