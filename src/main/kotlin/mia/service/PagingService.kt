/* PagingService.kt
* MIA - 인스타그램 자동화
* 페이지 번호 관련 클래스 - 당장은 안 씀
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.07
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.07     Service 생성
* ========================================================
*/

package mia.service

import org.springframework.stereotype.Service

@Service
class PagingService {

    companion object {
        private const val PAGE_LENGTH = 10
    }

    fun getPagingNumber(currentPage: Int, totalPages: Int): List<Int> {
        if (totalPages == 0) {
            return emptyList()
        }

        val start = (currentPage / PAGE_LENGTH) * PAGE_LENGTH
        val end = minOf(start + PAGE_LENGTH, totalPages)

        return (start until end).toList()
    }

}