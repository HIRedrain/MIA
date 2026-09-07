
(async () => {
  const pid = getPid(), root = document.querySelector("#detail-root");
  document.querySelector("#edit-link").href = `/admin/posts/${pid}/update`;
  try {
    const p = await api(`${API}/${pid}`);
    root.innerHTML = `
      <section class="card preview-card">
        ${p.imageUrl ? `<img class="preview-img" src="${esc(p.imageUrl)}" alt="${esc(p.productName)}">` : `<div class="preview-img"></div>`}
        <div class="preview-meta"><div class="product-title">${esc(p.productName)}</div><div class="muted">Instagram · ${fmtDate(p.instagramCreatedDate, true)}</div></div>
      </section>
      <section class="card panel">
        <h2 style="margin-bottom:10px">MIA 연결 정보</h2>
        <dl class="info-list">
          <div class="info-row"><dt>Post ID</dt><dd>#${p.postId}</dd></div>
<div class="info-row"><dt>Instagram 게시물</dt><dd>${p.instagramUrl ? `<a href="${esc(p.instagramUrl)}" target="_blank" rel="noreferrer">${esc(p.instagramUrl)}</a>` : "-"}</dd></div>          <div class="info-row"><dt>제품명</dt><dd>${esc(p.productName)}</dd></div>
          <div class="info-row"><dt>제품 URL</dt><dd><a href="${esc(p.productUrl)}" target="_blank" rel="noreferrer">${esc(p.productUrl)}</a></dd></div>
          <div class="info-row"><dt>키워드</dt><dd><span class="chip">${esc(p.keyword)}</span></dd></div>
          <div class="info-row"><dt>DM 메시지</dt><dd style="white-space:pre-wrap">${esc(p.dmMessage)}</dd></div>
          <div class="info-row"><dt>Instagram 게시일</dt><dd>${fmtDate(p.instagramCreatedDate, true)}</dd></div>
          <div class="info-row"><dt>MIA 등록일</dt><dd>${fmtDate(p.createdDate, true)}</dd></div>
          <div class="info-row"><dt>최근 수정일</dt><dd>${fmtDate(p.modifiedDate, true)}</dd></div>
        </dl>
      </section>`;
  } catch(e) { root.innerHTML = `<div class="notice notice-error">${esc(e.message)}</div>`; }
  document.querySelector("#delete-btn").onclick = async () => {
    const ok = await showConfirm({message:"MIA에서 이 게시물 연결 설정을 삭제합니다. Instagram 원본 게시물은 그대로 유지됩니다."});
    if (!ok) return;
    try { await api(`${API}/${pid}`, {method:"DELETE"}); location.href="/admin/posts"; }
    catch(e){ showToast(e.message); }
  };
})();
