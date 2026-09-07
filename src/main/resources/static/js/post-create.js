
document.querySelector("#post-form").addEventListener("submit", async e => {
  e.preventDefault();
  const btn = e.submitter; btn.disabled = true; btn.textContent = "등록 중...";
  const payload = {
    postURL: e.target.postURL.value.trim(),
    productName: e.target.productName.value.trim(),
    productURL: e.target.productURL.value.trim(),
    keyword: e.target.keyword.value.trim(),
    dmMessage: e.target.dmMessage.value.trim()
  };
  try {
    const created = await api(API, {method:"POST", body:JSON.stringify(payload)});
    showToast("게시물을 등록했습니다.");
    setTimeout(() => location.href = "/admin/posts", 400);
  } catch(err) {
    const duplicate = err.message.includes("이미 등록된") || err.message.includes("duplicate") || err.message.includes("Duplicate");
    showToast(duplicate ? "이미 등록된 인스타그램 게시물 주소입니다." : err.message);
    btn.disabled = false; btn.textContent = "등록하기";
  }
});
