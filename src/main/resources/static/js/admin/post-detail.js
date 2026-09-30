(async () => {
  const pid = getPid();
  const root = document.querySelector("#detail-root");

  try {
    const p = await api(`${API}/${pid}`);

    root.innerHTML = `
      <section class="card preview-card">
        ${
          p.imageUrl
            ? `<img class="preview-img" src="${esc(p.imageUrl)}" alt="${esc(p.productName)}">`
            : `<div class="preview-img"></div>`
        }

        <div class="preview-meta">
          <div class="product-title">${esc(p.productName)}</div>

          <div class="muted">
            Instagram · ${fmtDate(p.instagramCreatedDate, true)}
          </div>

          ${
            p.instagramUrl
              ? `
                <a
                  class="detail-instagram-link"
                  href="${esc(p.instagramUrl)}"
                  target="_blank"
                  rel="noreferrer"
                >
                  Instagram 게시물 보기
                </a>
              `
              : ""
          }

          <div class="detail-card-actions">
            <a
              id="detail-edit-link"
              class="btn btn-primary"
              href="/admin/posts/${pid}/update"
            >
              수정
            </a>

            <button
              id="detail-delete-btn"
              class="btn btn-danger"
              type="button"
            >
              삭제
            </button>
          </div>
        </div>
      </section>

      <section class="card panel">
        <h2 style="margin-bottom:10px">MIA 연결 정보</h2>

        <dl class="info-list">
          <div class="info-row">
            <dt>Post ID</dt>
            <dd>#${p.postId}</dd>
          </div>

          <div class="info-row">
            <dt>Instagram 게시물</dt>
            <dd>
              ${
                p.instagramUrl
                  ? `<a href="${esc(p.instagramUrl)}" target="_blank" rel="noreferrer">${esc(p.instagramUrl)}</a>`
                  : "-"
              }
            </dd>
          </div>

          <div class="info-row">
            <dt>제품명</dt>
            <dd>${esc(p.productName)}</dd>
          </div>

          <div class="info-row">
            <dt>제품 URL</dt>
            <dd>
              <a
                href="${esc(p.productUrl)}"
                target="_blank"
                rel="noreferrer"
              >
                ${esc(p.productUrl)}
              </a>
            </dd>
          </div>

          <div class="info-row">
            <dt>키워드</dt>
            <dd>
              <span class="chip">${esc(p.keyword)}</span>
            </dd>
          </div>

          <div class="info-row">
            <dt>DM 메시지</dt>
            <dd style="white-space:pre-wrap">${esc(p.dmMessage)}</dd>
          </div>

          <div class="info-row">
            <dt>Instagram 게시일</dt>
            <dd>${fmtDate(p.instagramCreatedDate, true)}</dd>
          </div>

          <div class="info-row">
            <dt>MIA 등록일</dt>
            <dd>${fmtDate(p.createdDate, true)}</dd>
          </div>

          <div class="info-row">
            <dt>최근 수정일</dt>
            <dd>${fmtDate(p.modifiedDate, true)}</dd>
          </div>
        </dl>
      </section>
    `;

    const deleteButton =
      document.querySelector("#detail-delete-btn");

    deleteButton?.addEventListener("click", async () => {
      const ok = await showConfirm({
        message:
          "MIA에서 이 게시물 연결 설정을 삭제합니다. Instagram 원본 게시물은 그대로 유지됩니다."
      });

      if (!ok) {
        return;
      }

      try {
        await api(`${API}/${pid}`, {
          method: "DELETE"
        });

        location.href = "/admin/posts";

      } catch (error) {
        showToast(error.message);
      }
    });

  } catch (error) {
    root.innerHTML =
      `<div class="notice notice-error">${esc(error.message)}</div>`;
  }
})();
