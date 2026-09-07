
(async () => {
  const list = document.querySelector("#recent-list");
  try {
    const page = await api(`${API}?page=0`);
    document.querySelector("#stat-total").textContent = page.totalElements ?? page.content?.length ?? 0;
    const recent = page.content?.[0];
    document.querySelector("#stat-recent").textContent = recent ? fmtDate(recent.instagramCreatedDate) : "-";
    if (!page.content?.length) {
      list.innerHTML = `<div class="empty">아직 등록된 게시물이 없습니다.</div>`; return;
    }
    list.innerHTML = `<div class="table-wrap"><table><thead><tr><th>게시물</th><th>제품명</th><th>키워드</th><th>Instagram 게시일</th><th></th></tr></thead><tbody>
      ${page.content.slice(0,5).map(p => `<tr>
      <td><img class="thumb" src="${esc(p.imageUrl || "")}" alt=""></td>
      <td><div class="product-title">${esc(p.productName)}</div><div class="muted">#${p.postId}</div></td>
      <td><span class="chip">${esc(p.keyword)}</span></td>
      <td>${fmtDate(p.instagramCreatedDate)}</td>
      <td><a class="btn btn-secondary" href="/admin/posts/${p.postId}">상세보기</a></td>
      </tr>`).join("")}
    </tbody></table></div>`;
  } catch(e) {
    list.innerHTML = `<div class="notice notice-error">${esc(e.message)}</div>`;
  }
})();
