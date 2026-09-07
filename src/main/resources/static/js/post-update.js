
const pid = getPid();
const form = document.querySelector("#post-form");
document.querySelector("#cancel-link").href = `/admin/posts/${pid}`;

(async () => {
  try {
    const p = await api(`${API}/${pid}`);
    form.productName.value = p.productName;
    form.productURL.value = p.productUrl;
    form.keyword.value = p.keyword;
    form.dmMessage.value = p.dmMessage;
    document.querySelector("#ig-preview").innerHTML = `<div class="instagram-preview">
      ${p.imageUrl ? `<img src="${esc(p.imageUrl)}" alt="${esc(p.productName)}">` : `<div class="preview-img"></div>`}
      <div><div class="eyebrow">Instagram original</div><h2 style="margin:4px 0 8px">${esc(p.productName)}</h2>
      <p class="subcopy">${p.instagramUrl ? `<a class="external-link" href="${esc(p.instagramUrl)}" target="_blank" rel="noreferrer">Instagram 게시물 열기 ↗</a><br>` : ""}게시일 · ${fmtDate(p.instagramCreatedDate, true)}</p>
      <p class="subcopy" style="margin-top:10px">Instagram 원본 정보와 이미지는 수정되지 않습니다.</p></div></div>`;
  } catch(e) { showToast(e.message); }
})();

form.addEventListener("submit", async e => {
  e.preventDefault();
  const btn = e.submitter; btn.disabled = true; btn.textContent = "저장 중...";
  const payload = {
    productName: form.productName.value.trim(),
    productURL: form.productURL.value.trim(),
    keyword: form.keyword.value.trim(),
    dmMessage: form.dmMessage.value.trim()
  };
  try {
    await api(`${API}/${pid}`, {method:"PUT", body:JSON.stringify(payload)});
    showToast("수정했습니다."); setTimeout(() => location.href=`/admin/posts/${pid}`, 350);
  } catch(err) { showToast(err.message); btn.disabled=false; btn.textContent="수정 완료"; }
});
