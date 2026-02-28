const deleteProfessorIdInput = document.getElementById('deleteProfessorId');
const formEdit = document.getElementById('edit-form');

// Regex password
const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;

// ----- Delete confirmation flow (teachers) -----
(function () {
    const confirmOverlay = document.getElementById('confirmDeleteOverlay');
    const confirmNameEl = document.getElementById('confirmName');
    const confirmCancel = document.getElementById('confirmCancel');
    const confirmDelete = document.getElementById('confirmDelete');
    const toastWrapTeacher = document.getElementById('toastWrapTeacher');

    // Ensure toast container is globally accessible if needed
    window.toastWrapTeacher = toastWrapTeacher;

    let pendingRow = null; // Stores the row that will be deleted

    // Opens the confirmation overlay
    function openConfirm() {
        if (!confirmOverlay) return;
        confirmOverlay.classList.add('show');
        confirmOverlay.setAttribute('aria-hidden', 'false');
    }

    // Closes the confirmation overlay
    function closeConfirm() {
        if (!confirmOverlay) return;
        confirmOverlay.classList.remove('show');
        confirmOverlay.setAttribute('aria-hidden', 'true');
        pendingRow = null;
    }

    // Listen for clicks on trash icons (event delegation per actions container)
    document.querySelectorAll('.actions').forEach(actions => {
        actions.addEventListener('click', e => {

            // Check if an image was clicked
            const img = e.target.closest('img');
            if (!img) return;

            // Ensure the clicked image is the trash icon
            const src = (img.getAttribute('src') || '').toLowerCase();
            if (!src.includes('trash')) return;

            // Find the corresponding row
            const row = img.closest('.student-row');
            if (!row) return;

            // Get teacher name (fallback to username if necessary)
            const nameEl = row.querySelector('.teacher-name') || row.querySelector('.teacher-username');
            const name = nameEl ? nameEl.textContent.trim() : 'este registro';

            // Store row and update confirmation modal
            const idInputRow = row.querySelector('.teacher-id');
            const teacherId = idInputRow ? idInputRow.value : null;
            pendingRow = row;
            confirmNameEl.textContent = name;
            if (deleteProfessorIdInput && teacherId) {
                deleteProfessorIdInput.value = teacherId;
            }
            openConfirm();
        });
    });

    // Cancel button (Voltar)
    if (confirmCancel) {
        confirmCancel.addEventListener('click', () => {
            closeConfirm();
        });
    }

    // Confirm delete button
    if (confirmDelete) {
        confirmDelete.addEventListener('click', () => {

            const id = document.getElementById('deleteProfessorId').value;
            const name = confirmNameEl.textContent.trim();

            window.location = `deleteProfessor?id=${id}&name=${encodeURIComponent(name)}`;

            // // Show success toast
            // const deletedName = confirmNameEl.textContent || 'Professor';
            //
            // if (typeof showToast === 'function') {
            //     showToast('teacher', 'success', 'Professor(a) excluído', `${deletedName} removido com sucesso.`);
            // } else {
            //
            //     // Fallback toast creation if showToast is not available
            //     const toast = document.createElement('div');
            //     toast.className = 'toast success';
            //     toast.innerHTML = `
            //         <img src="../Assets/check-icon.svg" style="width:30px;height:22px">
            //         <div class="toast-text">
            //             <h4>Professor(a) excluído</h4>
            //             <p>${deletedName} removido com sucesso.</p>
            //         </div>
            //     `;
            //
            //     toastWrapTeacher && toastWrapTeacher.appendChild(toast);
            //
            //     setTimeout(() => {
            //         toast.classList.add('hide');
            //         setTimeout(() => toast.remove(), 300);
            //     }, 4000);
            // }
        });
    }

    // Close overlay when clicking outside the modal
    if (confirmOverlay) {
        confirmOverlay.addEventListener('click', e => {
            if (e.target === confirmOverlay) closeConfirm();
        });
    }

    // Close overlay with ESC key
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape' && confirmOverlay && confirmOverlay.classList.contains('show')) {
            closeConfirm();
        }
    });

})();

(function () {
    // Elements
    const editOverlay = document.getElementById('editTeacherOverlay');
    const confirmOverlay = document.getElementById('confirmTeacherOverlay');
    const confirmTeacherSend = document.getElementById('confirmTeacherSend');
    const confirmTeacherCancel = document.getElementById('confirmTeacherCancel');
    const toastWrapTeacher = document.getElementById('toastWrapTeacher');

    if (!editOverlay || !confirmOverlay) {
        // required modals not found, abort gracefully
        return;
    }

    // Inputs inside edit modal (based on markup order)
    const editInputs = Array.from(editOverlay.querySelectorAll('input'));
    const editId = editInputs[0] || null; // Id
    const editUserInput = editInputs[1] || null;      // User
    const editNameInput = editInputs[2] || null;      // Name
    const editSubjectInput = editInputs[3] || null;   // Discipline
    const editPasswordInput = editInputs[4] || null;  // Password

    const btnEditCancel = editOverlay.querySelector('.btn-edit-cancel');
    const btnEditSave = editOverlay.querySelector('.btn-edit-save');

    // Keep reference to the row being edited
    let editingRow = null;

    // Helper: open/close overlay (matches your CSS 'show' behavior)
    function openOverlay(ov) {
        if (!ov) return;
        ov.classList.add('show');
        ov.setAttribute('aria-hidden', 'false');
    }
    function closeOverlay(ov) {
        if (!ov) return;
        ov.classList.remove('show');
        ov.setAttribute('aria-hidden', 'true');
    }

    // Helper: small toast fallback (used when global showToast isn't available)
    function fallbackToast(title, message, type = 'success') {
        if (!toastWrapTeacher) return;
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        const icon = type === 'error' ? contextPath + '/assets/error-icon.svg' : contextPath + '/assets/check-icon.svg';
        toast.innerHTML = `
            <img src="${icon}" style="width:30px;height:22px">
            <div class="toast-text">
                <h4>${title}</h4>
                ${message ? `<p>${message}</p>` : ''}
            </div>
        `;
        toastWrapTeacher.appendChild(toast);
        setTimeout(() => {
            toast.classList.add('hide');
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }

    function showToastTeacher(title, message, type = 'success') {
        // If a global showToast matching your other scripts exists, reuse it
        if (typeof window.showToast === 'function') {
            // many variants in your repo call: showToast('teacher','success', 'Title', 'message')
            // attempt to call in that shape first (owner, type, title, message)
            try {
                window.showToast('teacher', type, title, message);
                return;
            } catch (err) {
                // fallback to simpler call
            }
            try {
                window.showToast(title, message, type);
                return;
            } catch (err) {
                // continue to fallback
            }
        }

        // fallback
        fallbackToast(title, message, type);
    }

    // Fill edit modal with data from a row
    function populateEditModal(row) {
        if (!row) return;
        editingRow = row;

        const usernameEl = row.querySelector('.teacher-username');
        const nameEl = row.querySelector('.teacher-name');
        const subjectEl = row.querySelector('.teacher-subject');
        const passwordEl = row.querySelector('.teacher-password');

        const username = usernameEl ? usernameEl.textContent.trim() : '';
        const name = nameEl ? nameEl.textContent.trim() : '';
        const subject = subjectEl ? subjectEl.textContent.trim() : '';
        // don't read real password (it's masked) — leave input empty for security
        const pwd = '';

        if (editUserInput) editUserInput.value = username;
        if (editNameInput) editNameInput.value = name;
        if (editSubjectInput) editSubjectInput.value = subject;
        if (editPasswordInput) editPasswordInput.value = pwd;
    }

    // When clicking any edit icon in the actions, open edit modal (delegation)
    document.querySelectorAll('.actions').forEach(actions => {
        actions.addEventListener('click', (e) => {
            const img = e.target.closest('img');
            if (!img) return;

            const src = (img.getAttribute('src') || '').toLowerCase();
            // consider any edit icon filename containing 'edit' or 'icon-edit'
            if (!src.includes('edit')) return;

            const row = img.closest('.student-row');
            if (!row) return;

            populateEditModal(row);
            openOverlay(editOverlay);

            // focus first input
            setTimeout(() => {
                if (editUserInput) editUserInput.focus();
            }, 60);
        });
    });

    // Close edit modal via Cancel button
    if (btnEditCancel) {
        btnEditCancel.addEventListener('click', () => {
            editingRow = null;
            closeOverlay(editOverlay);
        });
    }

    // Click outside to close edit modal
    editOverlay.addEventListener('click', (e) => {
        if (e.target === editOverlay) {
            editingRow = null;
            closeOverlay(editOverlay);
        }
    });

    // ESC closes edit modal
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            // close both edit and confirm if open
            if (editOverlay.classList.contains('show')) {
                editingRow = null;
                closeOverlay(editOverlay);
            }
            if (confirmOverlay.classList.contains('show')) {
                closeOverlay(confirmOverlay);
            }
        }
    });

    // When user clicks SAVE on edit modal -> open confirm modal
    if (btnEditSave) {
        btnEditSave.addEventListener('click', (ev) => {
            ev.preventDefault();

            // basic validation: username & name required
            const userVal = editUserInput ? String(editUserInput.value || '').trim() : '';
            const nameVal = editNameInput ? String(editNameInput.value || '').trim() : '';
            const subjectVal = editSubjectInput ? String(editSubjectInput.value || '').trim() : '';
            const passwordVal = editPasswordInput ? String(editPasswordInput.value || '').trim() : '';

            if (!userVal || !nameVal || !subjectVal || !passwordVal) {
                showToastTeacher('Campos obrigatórios', 'Preencha todos os campos antes de continuar.', 'error');
                return;
            } else if (!regex.test(passwordVal)) {
                showToastTeacher('Senha inválida', 'Sua senha precisa conter pelo menos um número, uma letra e um caractere especial.','error');
                return;
            }

            openOverlay(confirmOverlay);
        });
    }

    // Cancel on confirm modal
    if (confirmTeacherCancel) {
        confirmTeacherCancel.addEventListener('click', () => {
            closeOverlay(confirmOverlay);
        });
    }

    // When confirming the save -> apply changes to DOM (no backend) and show toast
    if (confirmTeacherSend) {
        confirmTeacherSend.addEventListener('click', () => {
            // apply changes only to the editingRow in the DOM
            if (editingRow) {

                const idEl = editingRow.querySelector('.teacher-id');
                const usernameEl = editingRow.querySelector('.teacher-username');
                const nameEl = editingRow.querySelector('.teacher-name');
                const subjectEl = editingRow.querySelector('.teacher-subject');
                const passwordEl = editingRow.querySelector('.teacher-password');

                const id = idEl ? idEl.value : '';
                const userVal = editUserInput ? String(editUserInput.value || '').trim() : '';
                const nameVal = editNameInput ? String(editNameInput.value || '').trim() : '';
                const subjectVal = editSubjectInput ? String(editSubjectInput.value || '').trim() : '';
                const originalSubject = subjectEl ? subjectEl.textContent.trim() : '';

                if (subjectVal === originalSubject) {
                    editSubjectInput.value = '';
                }
                const pwdVal = editPasswordInput ? String(editPasswordInput.value || '').trim() : '';

                if (editId) editId.value = id;
                if (usernameEl) usernameEl.textContent = userVal || usernameEl.textContent;
                if (nameEl) nameEl.textContent = nameVal || nameEl.textContent;
                if (subjectEl) subjectEl.textContent = subjectVal || subjectEl.textContent;

                // For password, keep masked bullets for UI consistency
                if (passwordEl) {
                    if (pwdVal) passwordEl.textContent = '••••••••';
                    // if empty, leave it as it was
                }

            }

            formEdit.submit();
        });
    }

    // Close confirm overlay by clicking outside
    confirmOverlay.addEventListener('click', (e) => {
        if (e.target === confirmOverlay) closeOverlay(confirmOverlay);
    });

})();

// ----- Password toggle (edit modal) -----
(function () {

    const passwordWrap = document.querySelector('#editTeacherOverlay .password-input-wrap');
    if (!passwordWrap) return;

    const passwordInput = passwordWrap.querySelector('input');
    const toggleBtn = passwordWrap.querySelector('.password-toggle');
    const toggleImg = toggleBtn ? toggleBtn.querySelector('img') : null;

    if (!passwordInput || !toggleBtn || !toggleImg) return;

    toggleBtn.addEventListener('click', () => {

        const isHidden = passwordInput.type === 'password';

        // Toggle input type
        passwordInput.type = isHidden ? 'text' : 'password';

        // Swap icon
        toggleImg.src = isHidden
            ? contextPath + '/assets/eye.svg'
            : contextPath + '/assets/eye-off.svg';

    });

})();

function showToast(typeOwner, type, title, message) {
    const wrap =
        typeOwner === 'student' ? toastWrapStudent :
            typeOwner === 'teacher' ? toastWrapTeacher :
                null;

    if (!wrap) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const icon =
        type === 'error'
            ? contextPath + '/assets/error-icon.svg'
            : contextPath + '/assets/check-icon.svg';

    toast.innerHTML = `
        <img src="${icon}" style="width:30px;height:22px">
        <div class="toast-text">
        <h4>${title}</h4>
        ${message ? `<p>${message}</p>` : ''}
        </div>
    `;

    wrap.appendChild(toast);


    setTimeout(() => {
        toast.classList.add('hide');
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

window.showToast = showToast;