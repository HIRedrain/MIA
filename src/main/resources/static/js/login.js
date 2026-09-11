const loginForm = document.getElementById("loginForm");
const loginIdInput = document.getElementById("loginId");
const loginPwInput = document.getElementById("loginPw");
const loginButton = document.getElementById("loginButton");
const loginError = document.getElementById("loginError");
const passwordToggle = document.getElementById("passwordToggle");

let csrf = null;


document.addEventListener("DOMContentLoaded", async () => {
  const params = new URLSearchParams(location.search);

  if (params.get("signup") === "success") {
    showAuthToast(
      "회원가입이 완료되었습니다. 로그인해 주세요.",
      "success"
    );

    history.replaceState({}, "", "/login");
  }

  try {
    csrf = await getCsrfToken();
  } catch {
    showError(
      "로그인 정보를 준비하지 못했습니다. 새로고침 후 다시 시도해 주세요."
    );
  }
});


/*
 * 회원가입과 동일하게 로그인 아이디도
 * 영문 부분은 소문자로 통일합니다.
 */
loginIdInput.addEventListener("input", () => {
  const start = loginIdInput.selectionStart;
  const end = loginIdInput.selectionEnd;

  loginIdInput.value = loginIdInput.value.toLowerCase();

  if (start !== null && end !== null) {
    loginIdInput.setSelectionRange(start, end);
  }
});


passwordToggle.addEventListener("click", () => {
  const hidden = loginPwInput.type === "password";

  loginPwInput.type = hidden ? "text" : "password";

  passwordToggle.textContent = hidden ? "숨김" : "보기";
});


loginForm.addEventListener("submit", async event => {
  event.preventDefault();

  hideError();

  const loginId = loginIdInput.value
    .trim()
    .toLowerCase();

  /*
   * 회원가입과 반드시 같은 방식으로 변환해야
   * BCrypt matches() 대상 문자열이 동일해집니다.
   */
  const loginPw = koreanToEnglishKeyboard(
    loginPwInput.value
  );

  if (!loginId || !loginPw) {
    showError("아이디와 비밀번호를 입력해 주세요.");
    return;
  }

  setSubmitting(true);

  try {
    if (!csrf) {
      csrf = await getCsrfToken();
    }

    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        [csrf.headerName]: csrf.token
      },
      body: JSON.stringify({
        loginId,
        loginPw
      }),
      credentials: "same-origin"
    });

    if (!response.ok) {
      throw new Error(
        await readErrorMessage(
          response,
          "아이디 또는 비밀번호를 확인해 주세요."
        )
      );
    }

    const result = await response.json();

    // if (result.role === "ADMIN") {
    //   location.replace("/admin");
    //   return;
    // }

    location.replace("/");

  } catch (error) {

    showError(
      error.message || "로그인 중 오류가 발생했습니다."
    );

  } finally {
    setSubmitting(false);
  }
});


async function getCsrfToken() {
  const response = await fetch("/api/auth/csrf", {
    credentials: "same-origin"
  });

  if (!response.ok) {
    throw new Error("CSRF 토큰을 가져오지 못했습니다.");
  }

  return response.json();
}


async function readErrorMessage(response, fallback) {
  try {
    const body = await response.json();
    return body.message || body.error || fallback;
  } catch {
    return fallback;
  }
}


function setSubmitting(submitting) {
  loginButton.disabled = submitting;

  loginButton.textContent = submitting
    ? "로그인 중..."
    : "로그인";
}


function showError(message) {
  loginError.textContent = message;
  loginError.classList.remove("hidden");
}


function hideError() {
  loginError.textContent = "";
  loginError.classList.add("hidden");
}


/*
 * 회원가입 성공 후 로그인 페이지 상단에
 * 내려오는 성공 배너는 기존 방식 그대로 유지합니다.
 */
function showAuthToast(message, type = "default") {
  document.querySelector(".auth-toast")?.remove();

  const toast = document.createElement("div");

  toast.className = `auth-toast auth-toast-${type}`;
  toast.textContent = message;

  toast.setAttribute("role", "status");

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.classList.add("auth-toast-show");
  }, 10);

  setTimeout(() => {
    toast.classList.remove("auth-toast-show");

    setTimeout(() => {
      toast.remove();
    }, 180);
  }, 3200);
}


/*
 * signup.js와 동일한 두벌식 변환 함수입니다.
 * 추후 auth-common.js로 분리해도 되지만,
 * 현재 변경 범위를 최소화하기 위해 각 파일에 둡니다.
 */
function koreanToEnglishKeyboard(text) {

  const CHO = [
    "r", "R", "s", "e", "E", "f", "a", "q", "Q",
    "t", "T", "d", "w", "W", "c", "z", "x", "v", "g"
  ];

  const JUNG = [
    "k", "o", "i", "O", "j", "p", "u", "P", "h",
    "hk", "ho", "hl", "y", "n", "nj", "np", "nl",
    "b", "m", "ml", "l"
  ];

  const JONG = [
    "", "r", "R", "rt", "s", "sw", "sg", "e", "f",
    "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a",
    "q", "qt", "t", "T", "d", "w", "c", "z", "x",
    "v", "g"
  ];

  const JAMO = {
    "ㄱ": "r", "ㄲ": "R", "ㄳ": "rt", "ㄴ": "s",
    "ㄵ": "sw", "ㄶ": "sg", "ㄷ": "e", "ㄸ": "E",
    "ㄹ": "f", "ㄺ": "fr", "ㄻ": "fa", "ㄼ": "fq",
    "ㄽ": "ft", "ㄾ": "fx", "ㄿ": "fv", "ㅀ": "fg",
    "ㅁ": "a", "ㅂ": "q", "ㅃ": "Q", "ㅄ": "qt",
    "ㅅ": "t", "ㅆ": "T", "ㅇ": "d", "ㅈ": "w",
    "ㅉ": "W", "ㅊ": "c", "ㅋ": "z", "ㅌ": "x",
    "ㅍ": "v", "ㅎ": "g",

    "ㅏ": "k", "ㅐ": "o", "ㅑ": "i", "ㅒ": "O",
    "ㅓ": "j", "ㅔ": "p", "ㅕ": "u", "ㅖ": "P",
    "ㅗ": "h", "ㅘ": "hk", "ㅙ": "ho", "ㅚ": "hl",
    "ㅛ": "y", "ㅜ": "n", "ㅝ": "nj", "ㅞ": "np",
    "ㅟ": "nl", "ㅠ": "b", "ㅡ": "m", "ㅢ": "ml",
    "ㅣ": "l"
  };

  return [...text].map(char => {

    if (JAMO[char]) {
      return JAMO[char];
    }

    const code = char.charCodeAt(0);

    if (code < 0xAC00 || code > 0xD7A3) {
      return char;
    }

    const syllableIndex = code - 0xAC00;

    const choIndex = Math.floor(syllableIndex / 588);
    const jungIndex = Math.floor((syllableIndex % 588) / 28);
    const jongIndex = syllableIndex % 28;

    return (
      CHO[choIndex]
      + JUNG[jungIndex]
      + JONG[jongIndex]
    );
  }).join("");
}
