document.addEventListener("DOMContentLoaded", () => {
    initializeHome();
});


async function initializeHome() {

    const guestHeaderActions =
        document.getElementById("guestHeaderActions");

    const guestHeroActions =
        document.getElementById("guestHeroActions");

    const authHeaderActions =
        document.getElementById("authHeaderActions");

    const authHeroActions =
        document.getElementById("authHeroActions");

    const welcomeMessage =
        document.getElementById("welcomeMessage");


    /*
     * 기본 화면은 비로그인 상태로 설정합니다.
     *
     * JS 실행 중 문제가 생기더라도
     * 로그인 / 회원가입 버튼은 보이도록 합니다.
     */
    show(guestHeaderActions);
    show(guestHeroActions);

    hide(authHeaderActions);
    hide(authHeroActions);
    hide(welcomeMessage);


    try {

        /*
         * 현재 로그인 사용자 확인
         *
         * JWT는 HttpOnly Cookie에 있으므로
         * JavaScript에서 JWT를 직접 읽지 않습니다.
         *
         * 브라우저가 Cookie를 함께 보내고,
         * 서버의 /api/auth/me가 로그인 여부를 판단합니다.
         */
        const response = await fetch("/api/auth/me", {
            method: "GET",
            credentials: "same-origin"
        });


        /*
         * 로그인하지 않은 상태라면
         * /api/auth/me → 401
         *
         * 기본 비로그인 화면을 그대로 유지합니다.
         */
        if (!response.ok) {
            return;
        }


        /*
         * 로그인 상태
         *
         * 예)
         * {
         *   "loginId": "admin",
         *   "nickname": "관리자",
         *   "role": "ADMIN"
         * }
         */
        const user = await response.json();

        renderAuthenticatedHome(user);


    } catch (error) {

        console.error(
            "로그인 상태 확인 실패:",
            error
        );
    }
}


/*
 * 로그인 사용자용 홈 화면
 */
function renderAuthenticatedHome(user) {

    const guestHeaderActions =
        document.getElementById("guestHeaderActions");

    const guestHeroActions =
        document.getElementById("guestHeroActions");

    const authHeaderActions =
        document.getElementById("authHeaderActions");

    const authHeroActions =
        document.getElementById("authHeroActions");

    const welcomeMessage =
        document.getElementById("welcomeMessage");


    const userNickname =
        document.getElementById("userNickname");

    const welcomeNickname =
        document.getElementById("welcomeNickname");

    const userRole =
        document.getElementById("userRole");

    const userAvatar =
        document.getElementById("userAvatar");


    const adminPageButton =
        document.getElementById("adminPageButton");

    const logoutButton =
        document.getElementById("logoutButton");


    /*
     * 서버 응답에서 사용자 정보 추출
     */
    const nickname =
        user?.nickname
        ?? user?.userNickname
        ?? user?.loginId
        ?? "사용자";

    const role =
        user?.role
        ?? "CLIENT";


    /*
     * 비로그인용 UI 숨기기
     *
     * - 로그인
     * - 회원가입
     * - MIA 시작하기
     * - 계정 만들기
     */
    hide(guestHeaderActions);
    hide(guestHeroActions);


    /*
     * 로그인용 UI 표시
     *
     * - 사용자 정보
     * - 로그아웃
     * - MIA 둘러보기
     * - 환영 메시지
     */
    show(authHeaderActions);
    show(authHeroActions);
    show(welcomeMessage);


    /*
     * 닉네임 표시
     */
    if (userNickname) {
        userNickname.textContent = nickname;
    }


    /*
     * 환영 메시지 닉네임
     */
    if (welcomeNickname) {
        welcomeNickname.textContent = nickname;
    }


    /*
     * CLIENT / ADMIN 표시
     */
    if (userRole) {
        userRole.textContent = role;
    }


    /*
     * 사용자 아바타
     *
     * 닉네임 첫 글자를 사용합니다.
     *
     * 예)
     * 관리자 → 관
     * 김치 → 김
     * admin → A
     */
    if (userAvatar) {

        const firstCharacter =
            Array.from(
                String(nickname).trim()
            )[0] || "M";

        userAvatar.textContent =
            firstCharacter.toUpperCase();
    }


    /*
     * 관리자 전용 버튼
     *
     * ADMIN
     * → 관리자 페이지 버튼 표시
     *
     * CLIENT
     * → 관리자 페이지 버튼 숨김
     */
    if (adminPageButton) {

        if (role === "ADMIN") {
            show(adminPageButton);
        } else {
            hide(adminPageButton);
        }
    }


    /*
     * 로그아웃 버튼
     */
    if (logoutButton) {

        logoutButton.addEventListener(
            "click",
            handleLogout,
            { once: true }
        );
    }
}


/*
 * 로그아웃 처리
 */
async function handleLogout() {

    const logoutButton =
        document.getElementById("logoutButton");


    if (logoutButton) {
        logoutButton.disabled = true;
    }


    try {

        /*
         * POST 요청이므로
         * 먼저 CSRF 토큰을 가져옵니다.
         */
        const csrf =
            await getCsrfToken();


        const response =
            await fetch("/api/auth/logout", {

                method: "POST",

                headers: {
                    [csrf.headerName]: csrf.token
                },

                credentials: "same-origin"
            });


        if (!response.ok) {
            throw new Error(
                "로그아웃에 실패했습니다."
            );
        }


        /*
         * 로그아웃 성공
         *
         * JWT Cookie가 삭제된 후
         * 다시 홈으로 이동합니다.
         *
         * 새로 열린 홈에서는
         * /api/auth/me가 401을 반환하므로
         * 비로그인 홈이 표시됩니다.
         */
        location.replace("/");


    } catch (error) {

        console.error(
            "로그아웃 실패:",
            error
        );


        /*
         * 실패했으면 버튼을 다시 사용할 수 있도록 복구
         */
        if (logoutButton) {

            logoutButton.disabled = false;


            /*
             * 위에서 once:true를 사용했으므로
             * 실패했을 경우 이벤트를 다시 등록합니다.
             */
            logoutButton.addEventListener(
                "click",
                handleLogout,
                { once: true }
            );
        }
    }
}


/*
 * CSRF Token 조회
 */
async function getCsrfToken() {

    const response =
        await fetch("/api/auth/csrf", {

            method: "GET",

            credentials: "same-origin"
        });


    if (!response.ok) {

        throw new Error(
            "CSRF 토큰을 가져오지 못했습니다."
        );
    }


    return response.json();
}


/*
 * 요소 표시
 *
 * HTML hidden 속성을 제거합니다.
 */
function show(element) {

    if (element) {
        element.hidden = false;
    }
}


/*
 * 요소 숨기기
 *
 * HTML hidden 속성을 추가합니다.
 */
function hide(element) {

    if (element) {
        element.hidden = true;
    }
}