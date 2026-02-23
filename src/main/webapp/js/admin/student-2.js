const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;

// =========================
// TOAST
// =========================

const toastWrap = document.getElementById('toastWrapTeacher');

function createToast(title, message = '', type = 'success') {
    if (!toastWrap) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type === 'error' ? 'error' : 'success'}`;

    const icon = type === 'error'
        ? contextPath + '/assets/error-icon.svg'
        : contextPath + '/assets/check-icon.svg';

    toast.innerHTML = `
            <img src="${icon}" style="width:30px;height:22px" alt="">
            <div class="toast-text">
                <h4>${title}</h4>
                ${message ? `<p>${message}</p>` : ''}
            </div>
        `;

    toastWrap.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('hide');
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

window.createToast = createToast;

document.addEventListener('DOMContentLoaded', () => {

    // =========================
    // ELEMENTS
    // =========================
    const formEdit = document.getElementById("formEdit");

    const popupMenu = document.getElementById('popupMenu');
    const observationButtons = Array.from(document.querySelectorAll('.btn-observacoes'));

    const popupEdit = document.getElementById('popupEdit');
    const popupBulletin = document.getElementById('popupBulletin');
    const popupObservations = document.getElementById('popupObservations');
    const popupDelete = document.getElementById('popupDelete');

    const editOverlay = document.getElementById('editStudentOverlay');
    const inputId = document.getElementById('inputId')
    const inputName = document.getElementById('inputName');
    const inputEmail = document.getElementById('inputEmail');
    const inputAdmission = document.getElementById('inputAdmission');
    const inputSeries = document.getElementById('inputSeries');
    const inputPassword = document.getElementById('inputPassword');
    const editHeaderName = document.getElementById('editStudentNameHeader');

    const btnEditCancel = document.querySelector('.btn-edit-cancel');
    const btnEditSave = document.querySelector('.btn-edit-save');
    const togglePwd = document.getElementById('togglePwd');

    const confirmDeleteOverlay = document.getElementById('confirmDeleteOverlay');
    const confirmDeleteBtn = document.getElementById('confirmDelete');
    const confirmCancelBtn = document.getElementById('confirmCancel');
    const confirmName = document.getElementById('confirmName');

    const confirmSaveOverlay = document.getElementById('confirmTeacherOverlay');
    const confirmSaveBtn = document.getElementById('confirmTeacherSend');
    const confirmSaveCancelBtn = document.getElementById('confirmTeacherCancel');

    let activeRow = null;

    // =========================
    // OVERLAYS
    // =========================
    function openOverlay(el) {
        if (!el) return;
        el.classList.add('show');
        el.setAttribute('aria-hidden', 'false');
    }

    function closeOverlay(el) {
        if (!el) return;
        el.classList.remove('show');
        el.setAttribute('aria-hidden', 'true');
    }

    // =========================
    // POSITION POPUP
    // =========================
    function positionPopupNear(button) {
        if (!popupMenu || !button) return;

        const rect = button.getBoundingClientRect();
        popupMenu.style.left = rect.left - 150 + 'px';
        popupMenu.style.top = rect.bottom + window.scrollY + 10 + 'px';
    }

    // =========================
    // OPEN POPUP
    // =========================
    observationButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            activeRow = btn.closest('.student-row');
            positionPopupNear(btn);
            popupMenu.classList.toggle('show');
        });
    });

    document.addEventListener('click', () => {
        popupMenu.classList.remove('show');
    });

    popupMenu?.addEventListener('click', (e) => e.stopPropagation());

    // =========================
    // BULLETIN
    // =========================
    popupBulletin?.addEventListener('click', (e) => {
        e.stopPropagation();
        popupMenu.classList.remove('show');

        if (!activeRow) return;

        const matricula = activeRow.querySelector('.matricula')?.textContent.trim();
        window.location.href = `bulletin.html?matricula=${encodeURIComponent(matricula)}`;
    });

    // =========================
    // OBSERVATION
    // =========================
    popupObservations?.addEventListener('click', (e) => {
        e.stopPropagation();
        popupMenu.classList.remove('show');

        if (!activeRow) return;

        const matricula = activeRow.querySelector('.matricula')?.textContent.trim();
        window.location.href = `observation.html?matricula=${encodeURIComponent(matricula)}`;
    });

    // =========================
    // EDIT
    // =========================
    popupEdit?.addEventListener('click', (e) => {
        e.stopPropagation();
        popupMenu.classList.remove('show');

        if (!activeRow) return;

        const enrollment = activeRow.querySelector('.matricula')?.textContent.trim() || '';
        const name = activeRow.querySelector('.nome')?.textContent.trim() || '';
        const email = activeRow.querySelector('.email')?.textContent.trim() || '';
        const admission = activeRow.querySelector('.admissao')?.textContent.trim() || '';

        inputId.value = enrollment;
        editHeaderName.textContent = name;
        inputName.value = name;
        inputEmail.value = email;

        if (admission.includes('/')) {
            const [d, m, y] = admission.split('/');
            inputAdmission.value = `${y}-${m}-${d}`;
        }

        openOverlay(editOverlay);
    });

    btnEditCancel?.addEventListener('click', (e) => {
        e.preventDefault();
        closeOverlay(editOverlay);
        activeRow = null;
    });

    btnEditSave?.addEventListener('click', (e) => {
        e.preventDefault();

        if (!inputName.value.trim() || !inputEmail.value.trim() || !inputPassword.value.trim() || !inputAdmission.value.trim() || !inputSeries.value.trim()) {
            createToast('Campos obrigatórios', 'Preencha todos os campos antes de continuar.', 'error');
            return;
        } else if (!regex.test(inputPassword.value)) {
            createToast('Senha inválida', 'Sua senha precisa conter pelo menos um número, uma letra e um caractere especial.', 'error');
            return;
        }

        openOverlay(confirmSaveOverlay);
    });

    confirmSaveCancelBtn?.addEventListener('click', () => {
        closeOverlay(confirmSaveOverlay);
    });

    confirmSaveBtn?.addEventListener('click', () => {
        formEdit.submit();
    });

    // =========================
    // DELETE
    // =========================
    popupDelete?.addEventListener('click', (e) => {
        e.stopPropagation();
        popupMenu.classList.remove('show');

        if (!activeRow) return;

        confirmName.textContent = activeRow.querySelector('.nome')?.textContent.trim();
        openOverlay(confirmDeleteOverlay);
    });

    confirmCancelBtn?.addEventListener('click', () => {
        closeOverlay(confirmDeleteOverlay);
        activeRow = null;
    });

    confirmDeleteBtn?.addEventListener('click', () => {

        const name = confirmName.textContent;
        const enrollment = activeRow.querySelector('.matricula')?.textContent.trim();
        window.location = `deleteStudent?enrollment=${enrollment}&name=${encodeURIComponent(name)}`
    });

    // =========================
    // PASSWORD TOGGLE
    // =========================
    togglePwd?.addEventListener('click', (e) => {
        e.preventDefault();

        const img = togglePwd.querySelector('img');

        if (inputPassword.type === 'password') {
            inputPassword.type = 'text';
            img.src = contextPath + '/assets/eye.svg';
        } else {
            inputPassword.type = 'password';
            img.src = contextPath + '/assets/eye-off.svg';
        }
    });

    // =========================
    // CLOSE OVERLAY ON ESC
    // =========================
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            popupMenu.classList.remove('show');
            closeOverlay(editOverlay);
            closeOverlay(confirmDeleteOverlay);
            closeOverlay(confirmSaveOverlay);
        }
    });
});