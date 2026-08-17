/* AppConfig.kt
* MIA - 인스타그램 자동화
* Configuration
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.08.17
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.08.17     AppConfig 생성
* ========================================================
*/


package mia.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient


@Configuration
class AppConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }
}