
const API = "/api/posts";

function fmtDate(value, withTime = false) {
  if (!value) return "-";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return value;
  return new Intl.DateTimeFormat("ko-KR", {
    year: "numeric", month: "2-digit", day: "2-digit",
    ...(withTime ? {hour:"2-digit", minute:"2-digit"} : {})
  }).format(d);
}
function esc(v = "") {
  return String(v).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[m]));
}
function showToast(message) {
  const el = document.createElement("div");
  el.className = "toast"; el.textContent = message;
  document.body.appendChild(el);
  setTimeout(() => el.remove(), 2200);
}
function showConfirm({title="정말 삭제할까요?", message="삭제한 데이터는 되돌릴 수 없습니다.", confirmText="삭제"}) {
  return new Promise(resolve => {
    const wrap = document.createElement("div");
    wrap.className = "modal-backdrop";
    wrap.innerHTML = `<div class="modal">
      <h3>${esc(title)}</h3><p>${esc(message)}</p>
      <div class="modal-actions">
        <button class="btn btn-danger" data-confirm>${esc(confirmText)}</button>
        <button class="btn btn-secondary" data-cancel>취소</button>
      </div></div>`;
    document.body.appendChild(wrap);
    wrap.querySelector("[data-cancel]").onclick = () => { wrap.remove(); resolve(false); };
    wrap.querySelector("[data-confirm]").onclick = () => { wrap.remove(); resolve(true); };
    wrap.onclick = e => { if (e.target === wrap) { wrap.remove(); resolve(false); } };
  });
}
async function api(url, options = {}) {
  const res = await fetch(url, {
    headers: {"Content-Type":"application/json", ...(options.headers || {})},
    ...options
  });
  if (!res.ok) {
    let msg = `요청 실패 (${res.status})`;
    try { const b = await res.json(); msg = b.message || b.error || msg; } catch {}
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}
function getPid() {
  const match = location.pathname.match(/\/admin\/posts\/(\d+)/);
  return match ? Number(match[1]) : null;
}
