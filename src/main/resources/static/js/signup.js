const signupForm = document.getElementById("signupForm");
const loginIdInput = document.getElementById("loginId");
const nicknameInput = document.getElementById("userNickname");
const loginPwInput = document.getElementById("loginPw");
const loginPwConfirmInput = document.getElementById("loginPwConfirm");
const signupButton = document.getElementById("signupButton");
const passwordToggle = document.getElementById("passwordToggle");

let csrf = null;


document.addEventListener("DOMContentLoaded", async () => {
  try {
    csrf = await getCsrfToken();
  } catch {
    showSignupError(
      "회원가입 정보를 준비하지 못했습니다. 새로고침 후 다시 시도해 주세요."
    );
  }
});


/*
 * 아이디:
 * - 한글은 그대로 허용
 * - 영문은 소문자로 통일
 */
loginIdInput.addEventListener("input", () => {
  const start = loginIdInput.selectionStart;
  const end = loginIdInput.selectionEnd;

  loginIdInput.value = loginIdInput.value.toLowerCase();

  if (start !== null && end !== null) {
    loginIdInput.setSelectionRange(start, end);
  }
});

loginPwConfirmInput.addEventListener("compositionend", () => {
  loginPwConfirmInput.value =
      koreanToEnglishKeyboard(loginPwConfirmInput.value);
});

passwordToggle.addEventListener("click", () => {
  const hidden = loginPwInput.type === "password";

  loginPwInput.type = hidden ? "text" : "password";
  loginPwConfirmInput.type = hidden ? "text" : "password";

  passwordToggle.textContent = hidden ? "숨김" : "보기";
});


signupForm.addEventListener("submit", async event => {
  event.preventDefault();

  const loginId = loginIdInput.value
    .trim()
    .toLowerCase();

  const userNickname = nicknameInput.value.trim();

  /*
   * 한글 IME 상태에서 입력한 비밀번호도
   * 두벌식 영문 키 입력값으로 변환해서 서버로 전송합니다.
   *
   * 예)
   * 안녕 -> dkssud
   */
  const loginPw = koreanToEnglishKeyboard(loginPwInput.value);
  const loginPwConfirm = koreanToEnglishKeyboard(loginPwConfirmInput.value);

  if (!loginId || !userNickname || !loginPw || !loginPwConfirm) {
    showSignupError("모든 항목을 입력해 주세요.");
    return;
  }

  if (loginPw.length < 5) {
    showSignupError("비밀번호는 5자 이상 입력해 주세요.");
    return;
  }

  if (loginPw !== loginPwConfirm) {
    showSignupError("비밀번호가 서로 일치하지 않습니다.");
    loginPwConfirmInput.focus();
    return;
  }

  setSubmitting(true);

  try {
    if (!csrf) {
      csrf = await getCsrfToken();
    }

    const response = await fetch("/api/user", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        [csrf.headerName]: csrf.token
      },
      credentials: "same-origin",
      body: JSON.stringify({
        loginId,
        loginPw,
        userNickname
      })
    });

    if (!response.ok) {
      throw new Error(
        await readErrorMessage(
          response,
          "회원가입에 실패했습니다."
        )
      );
    }

    location.replace("/login?signup=success");

  } catch (error) {

    showSignupError(
      error.message || "회원가입 중 오류가 발생했습니다."
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
  signupButton.disabled = submitting;
  signupButton.textContent = submitting
    ? "가입 중..."
    : "회원가입";
}


/*
 * 회원가입 페이지 전용 오류 토스트.
 *
 * 성공 토스트(auth-toast)는 로그인 페이지 상단 배너로 유지하고,
 * 회원가입 중 발생하는 오류만 signup-card의 정중앙에 표시합니다.
 */
function showSignupError(message) {
  const card = document.querySelector(".signup-card");

  if (!card) {
    return;
  }

  card.querySelector(".signup-error-toast")?.remove();

  const toast = document.createElement("div");

  toast.className = "signup-error-toast";
  toast.textContent = message;

  toast.setAttribute("role", "alert");
  toast.setAttribute("aria-live", "assertive");

  card.appendChild(toast);

  requestAnimationFrame(() => {
    toast.classList.add("signup-error-toast-show");
  });

  window.setTimeout(() => {
    toast.classList.remove("signup-error-toast-show");

    window.setTimeout(() => {
      toast.remove();
    }, 180);
  }, 2400);
}


/*
 * 완성형 한글 + 호환 자모를
 * 표준 두벌식 영문 키 입력값으로 변환합니다.
 *
 * 이미 영문/숫자/특수문자로 입력된 문자는 그대로 유지합니다.
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
