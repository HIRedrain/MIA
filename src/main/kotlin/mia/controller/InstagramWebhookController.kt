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
* 이홍비    2026.08.09     Controller 생성
* 이홍비    2026.08.09     meta api 쪽 webhooks 연결 확인
* 이홍비    2026.08.12     Webhook 본격 연결
* ========================================================
*/

package mia.controller

import mia.dto.CommentWebhookRequest
import mia.dto.InstagramWebhookRequest
import mia.service.InstagramCommentService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/webhook")
class InstagramWebhookController (
    private val instagramCommentService: InstagramCommentService
) {
//    @PostMapping
//    fun receive(
//        @RequestBody request: CommentWebhookRequest
//    ) {
//        instagramCommentService.process(request)
//    }

//    @PostMapping
//    fun receiveWebhook(
//        @RequestBody body: String
//    ) {
//        println("🔥 WEBHOOK")
//        println(body)
//    }


    @PostMapping
    fun receive(
        @RequestBody request: InstagramWebhookRequest
    ) {

        // request: InstagramWebhookRequest - 외부 DTO
        request.entry.forEach { entry ->

            entry.changes.forEach { change ->

                if (change.field != "comments") {
                    return@forEach
                }

                // 내부 DTO 로 변환
                val comment = change.value
                val commentWebhookRequest = CommentWebhookRequest(
                    mediaId = comment.media.id,
                    commentId = comment.id,
                    commenterId = comment.from.id,
                    commentText = comment.text
                )
                instagramCommentService.process(commentWebhookRequest)
            }

        }
    }


    // 내가 설정한 webhook 인증 토큰
    @Value("\${meta.instagram.verify-token}")
    lateinit var verifyToken: String

    // 메타 서버가 웹훅 등록 시 유효성 검증을 위해 보내는 GET 요청 처리
    @GetMapping()
    fun verifyWebhook(
        @RequestParam("hub.mode") mode: String,
        @RequestParam("hub.verify_token") token: String,
        @RequestParam("hub.challenge") challenge: String
    ): String {

        println("✅ verifyToken : $verifyToken")
        println("✅ token : $token")
        println("✅ challenge : $challenge")

        if (token == verifyToken) {
            // 검증 성공 시 메타가 보낸 challenge 값을 그대로 텍스트로 반환

            return challenge
        }

        throw RuntimeException("Invalid Verify Token") // 토큰이 틀리면 예외 던짐
    }

}