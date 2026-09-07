
let currentPage = 0;
let currentData = null;

function renderRows(items) {
  const root = document.querySelector("#posts-root");
  if (!items.length) { root.innerHTML = `<div class="empty">조건에 맞는 게시물이 없습니다.</div>`; return; }
  root.innerHTML = `<div class="table-wrap"><table>
    <thead><tr><th>No.</th><th>이미지</th><th>제품명</th><th>키워드</th><th>Instagram 게시일</th><th>관리</th></tr></thead>
    <tbody>${items.map(p => `<tr>
      <td>#${p.postId}</td>
      <td>${p.imageUrl ? `<img class="thumb" src="${esc(p.imageUrl)}" alt="${esc(p.productName)}">` : `<div class="thumb"></div>`}</td>
      <td><a class="product-link" href="/admin/posts/${p.postId}"><div class="product-title">${esc(p.productName)}</div></a><div class="muted">${esc(p.productUrl)}</div></td>
      <td><span class="chip">${esc(p.keyword)}</span></td>
      <td>${fmtDate(p.instagramCreatedDate)}</td>
      <td><div class="row-actions">
        <a class="icon-btn" title="상세보기" href="/admin/posts/${p.postId}">⌕</a>
        <a class="icon-btn" title="수정" href="/admin/posts/${p.postId}/update">✎</a>
        <button class="icon-btn danger-icon" title="삭제" aria-label="삭제" data-delete="${p.postId}">🗑</button>
      </div></td>
    </tr>`).join("")}</tbody></table></div>`;
  root.querySelectorAll("[data-delete]").forEach(btn => btn.onclick = async () => {
    const pid = btn.dataset.delete;
    const ok = await showConfirm({message:"이 게시물의 MIA 연결 설정을 삭제합니다. Instagram 원본 게시물은 삭제되지 않습니다."});
    if (!ok) return;
    try { await api(`${API}/${pid}`, {method:"DELETE"}); showToast("게시물을 삭제했습니다."); load(currentPage); }
    catch(e){ showToast(e.message); }
  });
}
function renderPagination(page) {
  const el = document.querySelector("#pagination");
  const total = page.totalPages || 0;
  if (total <= 1) { el.innerHTML = ""; return; }
  const block = 10, start = Math.floor(page.number / block) * block, end = Math.min(start + block, total);
  let html = `<button class="page-btn" data-page="${page.number-1}" ${page.first?'disabled':''}>‹</button>`;
  for(let i=start;i<end;i++) html += `<button class="page-btn ${i===page.number?'active':''}" data-page="${i}">${i+1}</button>`;
  html += `<button class="page-btn" data-page="${page.number+1}" ${page.last?'disabled':''}>›</button>`;
  el.innerHTML = html;
  el.querySelectorAll("[data-page]").forEach(b => b.onclick = () => { if(!b.disabled) load(Number(b.dataset.page)); });
}
async function load(page=0) {
  currentPage = page;
  try {
    currentData = await api(`${API}?page=${page}`);
    document.querySelector("#count").textContent = `총 ${currentData.totalElements ?? currentData.content.length}개`;
    renderRows(currentData.content || []);
    renderPagination(currentData);
  } catch(e) {
    document.querySelector("#posts-root").innerHTML = `<div class="notice notice-error">${esc(e.message)}</div>`;
  }
}
document.querySelector("#search").addEventListener("input", e => {
  const q = e.target.value.trim().toLowerCase();
  const items = (currentData?.content || []).filter(p =>
    p.productName.toLowerCase().includes(q) || p.keyword.toLowerCase().includes(q)
  );
  renderRows(items);
});
load();
