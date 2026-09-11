/* SecurityConfig.kt
* MIA - 인스타그램 자동화
* 로그인, 토근, path permit 관련 config
* 작성자 : 이홍비
* 최초 작성 날짜 : 2026.09.09
*
* ========================================================
* 프로그램 수정 / 보완 이력
* ========================================================
* 작업자        날짜        수정 / 보완 내용
* ========================================================
* 이홍비    2026.09.09     SecurityConfig 생성
* ========================================================
*/

package mia.config

import mia.security.JwtAuthenticationFilter
import mia.security.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse().apply {
            setCookiePath("/")
        }

        http
            .csrf { csrf ->
                csrf
                    .csrfTokenRepository(csrfRepository)
                    .ignoringRequestMatchers("/webhook") // CSRF 검사 제외
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/login",
                        "/api/auth/login",
                        "/api/auth/csrf",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico",
                        "/error"
                    ).permitAll()
                    .requestMatchers("/webhook").permitAll() // 로그인 인증 없이 접근 허용
                    .requestMatchers(HttpMethod.POST,"/api/user").permitAll()
                    .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/auth/me", "/api/auth/logout").authenticated()
                    .anyRequest().permitAll()
            }
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { request, response, _ ->
                    if (request.requestURI.startsWith("/api/")) {
                        response.status = HttpStatus.UNAUTHORIZED.value()
                        response.contentType = "application/json;charset=UTF-8"
                        response.writer.write("{\"message\":\"로그인이 필요합니다.\"}")
                    } else {
                        response.sendRedirect("/login")
                    }
                }

                exception.accessDeniedHandler { request, response, _ ->
                    if (request.requestURI.startsWith("/api/")) {
                        response.status = HttpStatus.FORBIDDEN.value()
                        response.contentType = "application/json;charset=UTF-8"
                        response.writer.write("{\"message\":\"접근 권한이 없습니다.\"}")
                    } else {
                        response.sendRedirect("/login")
                    }
                }
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}
