const API = "/api/admin/posts";

function fmtDate(value, withTime = false) {
  if (!value) return "-";

  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return value;

  return new Intl.DateTimeFormat("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    ...(withTime ? { hour: "2-digit", minute: "2-digit" } : {})
  }).format(d);
}

function esc(v = "") {
  return String(v).replace(
    /[&<>"']/g,
    m => ({
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#039;"
    })[m]
  );
}

function getCookie(name) {
  const prefix = `${encodeURIComponent(name)}=`;

  const cookie = document.cookie
    .split("; ")
    .find(row => row.startsWith(prefix));

  return cookie
    ? decodeURIComponent(cookie.substring(prefix.length))
    : null;
}

async function ensureCsrfToken() {
  const existing = getCookie("XSRF-TOKEN");

  if (existing) {
    return existing;
  }

  const response = await fetch("/api/auth/csrf", {
    credentials: "same-origin"
  });

  if (!response.ok) {
    throw new Error("보안 토큰을 가져오지 못했습니다.");
  }

  const body = await response.json();
  return body.token;
}

async function api(url, options = {}) {
  const method = (options.method || "GET").toUpperCase();

  const headers = {
    ...(options.body ? { "Content-Type": "application/json" } : {}),
    ...(options.headers || {})
  };

  if (!["GET", "HEAD", "OPTIONS", "TRACE"].includes(method)) {
    headers["X-XSRF-TOKEN"] = await ensureCsrfToken();
  }

  const res = await fetch(url, {
    ...options,
    method,
    headers,
    credentials: "same-origin"
  });

  if (res.status === 401) {
    location.replace("/login");
    throw new Error("로그인이 만료되었습니다.");
  }

  if (!res.ok) {
    let msg = `요청 실패 (${res.status})`;

    try {
      const body = await res.json();
      msg = body.message || body.error || msg;
    } catch {}

    throw new Error(msg);
  }

  if (res.status === 204) return null;

  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

async function logout() {
  try {
    const csrfToken = await ensureCsrfToken();

    await fetch("/api/auth/logout", {
      method: "POST",
      headers: {
        "X-XSRF-TOKEN": csrfToken
      },
      credentials: "same-origin"
    });
  } finally {
    location.replace("/");
  }
}

function showToast(message) {
  document.querySelector(".toast")?.remove();

  const el = document.createElement("div");
  el.className = "toast";
  el.setAttribute("role", "status");
  el.setAttribute("aria-live", "polite");
  el.textContent = message;

  document.body.appendChild(el);

  setTimeout(() => {
    el.classList.add("toast-hide");
    setTimeout(() => el.remove(), 180);
  }, 3200);
}

function showConfirm({
  title = "정말 삭제할까요?",
  message = "삭제한 데이터는 되돌릴 수 없습니다.",
  confirmText = "삭제"
}) {
  return new Promise(resolve => {
    const wrap = document.createElement("div");
    wrap.className = "modal-backdrop";

    wrap.innerHTML = `
      <div class="modal" role="dialog" aria-modal="true">
        <h3>${esc(title)}</h3>
        <p>${esc(message)}</p>

        <div class="modal-actions">
          <button class="btn btn-danger" data-confirm>${esc(confirmText)}</button>
          <button class="btn btn-secondary" data-cancel>취소</button>
        </div>
      </div>
    `;

    document.body.appendChild(wrap);

    const close = value => {
      wrap.remove();
      resolve(value);
    };

    wrap.querySelector("[data-cancel]").onclick = () => close(false);
    wrap.querySelector("[data-confirm]").onclick = () => close(true);

    wrap.onclick = e => {
      if (e.target === wrap) close(false);
    };
  });
}

function getPid() {
  const match = location.pathname.match(/\/admin\/posts\/(\d+)/);
  return match ? Number(match[1]) : null;
}

async function loadCurrentUser() {
  const nicknameEl = document.querySelector("[data-user-nickname]");
  const roleEl = document.querySelector("[data-user-role]");
  const avatarEl = document.querySelector("[data-user-avatar]");

  if (!nicknameEl && !roleEl && !avatarEl) {
    return;
  }

  try {
    const user = await api("/api/auth/me");

    const nickname = user?.nickname
      ?? user?.userNickname
      ?? user?.loginId
      ?? "사용자";

    const role = user?.role ?? "";

    if (nicknameEl) {
      nicknameEl.textContent = nickname;
    }

    if (roleEl) {
      roleEl.textContent = role;
    }

    if (avatarEl) {
      const first = [...String(nickname).trim()][0] || "M";
      avatarEl.textContent = first.toUpperCase();
    }
  } catch (error) {
    console.error("사용자 정보를 불러오지 못했습니다.", error);
  }
}

function bindSidebarLogout() {
  const button = document.querySelector("[data-logout-button]");

  if (!button) {
    return;
  }

  button.addEventListener("click", logout);
}

document.addEventListener("DOMContentLoaded", () => {
  bindSidebarLogout();
  loadCurrentUser();
});
