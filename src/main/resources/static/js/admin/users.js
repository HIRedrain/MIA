const USER_API = "/api/admin/users";
let currentUserPage = 0;
let currentUserData = null;

function roleLabel(role) {
  return role === "ADMIN" ? "관리자" : "회원";
}

function renderUserRows(items) {
  const root = document.querySelector("#users-root");

  if (!items.length) {
    root.innerHTML = `<div class="empty">조건에 맞는 회원이 없습니다.</div>`;
    return;
  }

  root.innerHTML = `
    <div class="table-wrap">
      <table class="user-table">
        <thead>
          <tr>
            <th>No.</th>
            <th>별명</th>
            <th>아이디</th>
            <th>권한</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          ${items.map(user => `
            <tr>
              <td>#${user.userId}</td>
              <td>
                <a class="product-link" href="/admin/users/${user.userId}">
                  <div class="product-title">${esc(user.nickname)}</div>
                </a>
              </td>
              <td>${esc(user.loginId)}</td>
              <td><span class="role-badge ${user.role === "ADMIN" ? "role-admin" : "role-client"}">${roleLabel(user.role)}</span></td>
              <td>
                <div class="row-actions">
                  <a class="icon-btn" title="상세보기" aria-label="상세보기" href="/admin/users/${user.userId}">⌕</a>
                </div>
              </td>
            </tr>
          `).join("")}
        </tbody>
      </table>
    </div>`;
}

function renderUserPagination(page) {
  const el = document.querySelector("#user-pagination");
  const total = page.totalPages || 0;

  if (total <= 1) {
    el.innerHTML = "";
    return;
  }

  const block = 10;
  const start = Math.floor(page.page / block) * block;
  const end = Math.min(start + block, total);

  // page.number => page.page 로 수정
  let html = `<button class="page-btn" data-page="${page.page - 1}" ${page.first ? "disabled" : ""}>‹</button>`;
  for (let i = start; i < end; i++) {
    html += `<button class="page-btn ${i === page.page ? "active" : ""}" data-page="${i}">${i + 1}</button>`;
  }
  html += `<button class="page-btn" data-page="${page.page + 1}" ${page.last ? "disabled" : ""}>›</button>`;

  el.innerHTML = html;
  el.querySelectorAll("[data-page]").forEach(button => {
    button.onclick = () => {
      if (!button.disabled) loadUsers(Number(button.dataset.page));
    };
  });
}

async function loadUsers(page = 0) {
  currentUserPage = page;

  try {
    currentUserData = await api(`${USER_API}?page=${page}&size=10`);
    document.querySelector("#user-count").textContent = `총 ${currentUserData.totalElements ?? currentUserData.content.length}명`;
    renderUserRows(currentUserData.content || []);
    renderUserPagination(currentUserData);
  } catch (error) {
    document.querySelector("#users-root").innerHTML = `<div class="notice notice-error">${esc(error.message)}</div>`;
  }
}

document.querySelector("#user-search").addEventListener("input", event => {
  const query = event.target.value.trim().toLowerCase();
  const items = (currentUserData?.content || []).filter(user =>
    user.loginId.toLowerCase().includes(query) ||
    user.nickname.toLowerCase().includes(query)
  );
  renderUserRows(items);
});

loadUsers();
