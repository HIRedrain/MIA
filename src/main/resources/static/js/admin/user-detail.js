const USER_API = "/api/admin/users";

function getUserId() {
  const match = location.pathname.match(/\/admin\/users\/(\d+)/);
  return match ? Number(match[1]) : null;
}

function roleLabel(role) {
  return role === "ADMIN" ? "관리자" : "회원";
}

(async () => {
  const userId = getUserId();
  const root = document.querySelector("#user-detail-root");

  if (!userId) {
    root.innerHTML = `<div class="notice notice-error">잘못된 회원 번호입니다.</div>`;
    return;
  }

  try {
    const [user, me] = await Promise.all([
      api(`${USER_API}/${userId}`),
      api("/api/auth/me")
    ]);

    const isSelf = me?.loginId === user.loginId;
    const nextRole = user.role === "ADMIN" ? "CLIENT" : "ADMIN";
    const roleActionText = user.role === "ADMIN" ? "회원으로 변경" : "관리자로 변경";

    root.innerHTML = `
      <div class="grid user-detail-layout">
        <section class="card user-profile-card">
          <div class="user-profile-avatar">${esc(([...String(user.nickname || user.loginId)][0] || "M").toUpperCase())}</div>
          <div class="user-profile-name">${esc(user.nickname)}</div>
          <div class="user-profile-id">${esc(user.loginId)}</div>
          <span class="role-badge ${user.role === "ADMIN" ? "role-admin" : "role-client"}">${roleLabel(user.role)}</span>
          ${isSelf ? `<div class="self-account-note">현재 로그인한 계정입니다.</div>` : ""}
        </section>

        <section class="card panel">
          <h2 style="margin-bottom:10px">회원 정보</h2>
          <dl class="info-list">
            <div class="info-row"><dt>회원 번호</dt><dd>#${user.userId}</dd></div>
            <div class="info-row"><dt>아이디</dt><dd>${esc(user.loginId)}</dd></div>
            <div class="info-row"><dt>별명</dt><dd>${esc(user.nickname)}</dd></div>
            <div class="info-row"><dt>권한</dt><dd><span class="role-badge ${user.role === "ADMIN" ? "role-admin" : "role-client"}">${roleLabel(user.role)}</span></dd></div>
          </dl>

          <div class="user-management-actions">
            <div>
              <strong>권한 관리</strong>
              <p>회원과 관리자 권한을 변경합니다.</p>
            </div>
            <button id="role-change-button" class="btn btn-secondary" type="button" ${isSelf ? "disabled" : ""}>${roleActionText}</button>
          </div>

          <div class="user-management-actions danger-zone">
            <div>
              <strong>강제 탈퇴</strong>
              <p>회원 계정을 삭제합니다. 삭제 후에는 복구할 수 없습니다.</p>
            </div>
            <button id="user-delete-button" class="btn btn-danger" type="button" ${isSelf ? "disabled" : ""}>강제 탈퇴</button>
          </div>
        </section>
      </div>`;

    document.querySelector("#role-change-button")?.addEventListener("click", async () => {
      const ok = await showConfirm({
        title: "권한을 변경하시겠습니까?",
        message: `'${user.nickname}' 회원의 권한을 ${roleLabel(user.role)}에서 ${roleLabel(nextRole)}(으)로 변경합니다.`,
        confirmText: "변경"
      });
      if (!ok) return;

      try {
        await api(`${USER_API}/${userId}/role`, {
          method: "PATCH",
          body: JSON.stringify({ role: nextRole })
        });
        showToast(`회원 권한을 ${roleLabel(nextRole)}(으)로 변경했습니다.`);
        setTimeout(() => location.reload(), 700);
      } catch (error) {
        showToast(error.message);
      }
    });

    document.querySelector("#user-delete-button")?.addEventListener("click", async () => {
      const ok = await showConfirm({
        title: "회원을 강제 탈퇴시킬까요?",
        message: `'${user.nickname}' 회원의 계정을 삭제합니다. 삭제된 회원 정보는 복구할 수 없습니다.`,
        confirmText: "탈퇴"
      });
      if (!ok) return;

      try {
        await api(`${USER_API}/${userId}`, { method: "DELETE" });
        location.href = "/admin/users?deleted=1";
      } catch (error) {
        showToast(error.message);
      }
    });
  } catch (error) {
    root.innerHTML = `<div class="notice notice-error">${esc(error.message)}</div>`;
  }
})();
