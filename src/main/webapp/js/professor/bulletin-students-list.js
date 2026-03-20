// ===== DOM ELEMENT REFERENCES ===== //

// Main grade modal overlay
const gradeOverlay = document.getElementById('gradeOverlay');

// Confirmation modal overlay
const confirmOverlay = document.getElementById('confirmOverlay');

// Toast notifications container
const toastWrap = document.querySelector('.toast-wrap');

// Grade input fields
const nota1Input = document.getElementById('nota1');
const nota2Input = document.getElementById('nota2');

// Average grade display
const avgValue = document.getElementById('avgValue');

// Admin warning hint
const saveHint = document.getElementById('saveHint');

// Student information in modal
const gradeTitle = document.getElementById('gradeTitle');
const gradeEmail = document.getElementById('gradeEmail');
const gradeMat = document.getElementById('gradeMat');

// Confirmation modal data
const confirmStudent = document.getElementById('confirmStudent');
const confirmAvg = document.getElementById('confirmAvg');

// Currently selected student row
let currentRow = null;

let formEdit = document.getElementById("formEdit");
let formEditRec = document.getElementById("formEditRec");

// ===== GLOBAL CLICK HANDLER ===== //

// Detect clicks on "add grade" buttons
document.addEventListener('click', (e) => {
    const plus = e.target.closest('.action-btn.plus');
    if (!plus || plus.classList.contains('disabled')) return;

    const row = plus.closest('.student-row');
    openGradeModal(row);
});

// ===== MODAL CONTROL FUNCTIONS ===== //

// Open grade modal and populate student data
function openGradeModal(row) {
    currentRow = row;

    const nota1 = (row.dataset.n1 || '').replace(",", ".");
    const nota2 = (row.dataset.n2 || '').replace(",", ".");
    const enrollment = row.dataset.enrollment;

    gradeTitle.textContent = row.querySelector('.nome').textContent.trim();
    gradeEmail.textContent = row.querySelector('.email').textContent.trim();
    gradeMat.textContent = row.querySelector('.matricula').textContent.trim();

    document.getElementById("nota1").value = nota1 !== "-1.0" ? nota1 : "";
    document.getElementById("nota2").value = nota2 !== "-1.0" ? nota2 : "";
    document.getElementById("enrollmentGrades").value = enrollment;

    nota1Input.setCustomValidity('');
    nota2Input.setCustomValidity('');
    saveHint.style.display = 'none';

    calcAvg();

    gradeOverlay.classList.add('show');
    gradeOverlay.setAttribute('aria-hidden', 'false');
    nota1Input.focus();
}

// Close grade modal
function closeGradeModal() {
    gradeOverlay.classList.remove('show');
    gradeOverlay.setAttribute('aria-hidden', 'true');
}

// Close confirmation modal
function closeConfirmModal() {
    confirmOverlay.classList.remove('show');
    confirmOverlay.setAttribute('aria-hidden', 'true');
}

// ===== GRADE UTILITIES ===== //

function getNormalizedValue(input) {
    return input.value.trim().replace(',', '.');
}

// Parse numeric grade value safely
function getGradeValue(input) {
    const raw = getNormalizedValue(input);

    if (raw === '') return null;

    const value = Number(raw);
    return isNaN(value) ? null : value;
}

// Checks if input is empty or valid between 0 and 10
function isValidGradeInput(input) {
    const raw = getNormalizedValue(input);

    if (raw === '') return true;

    const value = Number(raw);
    return !isNaN(value) && value >= 0 && value <= 10;
}

// Show native error message in input
function setGradeError(input, message = '') {
    input.setCustomValidity(message);
    input.reportValidity();
}

// Clamp grade value between 0 and 10
function clampGrade(input) {
    const value = getGradeValue(input);

    if (value === null) {
        input.value = '';
        input.setCustomValidity('');
        return;
    }

    let fixedValue = value;

    if (fixedValue > 10) fixedValue = 10;
    if (fixedValue < 0) fixedValue = 0;

    input.value = fixedValue.toFixed(1).replace('.0', '');
    input.setCustomValidity('');
}

// Calculate average value only if both notes are valid
function calculateAverageValue() {
    const v1 = getGradeValue(nota1Input);
    const v2 = getGradeValue(nota2Input);

    if (v1 === null || v2 === null) return null;
    if (!isValidGradeInput(nota1Input) || !isValidGradeInput(nota2Input)) return null;

    return ((v1 + v2) / 2).toFixed(1);
}

// Update average display dynamically
function calcAvg() {
    const raw1 = nota1Input.value.trim();
    const raw2 = nota2Input.value.trim();

    if (!raw1 && !raw2) {
        avgValue.textContent = '—';
        nota1Input.setCustomValidity('');
        nota2Input.setCustomValidity('');
        return;
    }

    let hasError = false;

    if (!isValidGradeInput(nota1Input)) {
        setGradeError(nota1Input, 'Digite uma nota entre 0 e 10.');
        hasError = true;
    } else {
        nota1Input.setCustomValidity('');
    }

    if (!isValidGradeInput(nota2Input)) {
        setGradeError(nota2Input, 'Digite uma nota entre 0 e 10.');
        hasError = true;
    } else {
        nota2Input.setCustomValidity('');
    }

    if (hasError) {
        avgValue.textContent = '—';
        return;
    }

    const avg = calculateAverageValue();
    avgValue.textContent = avg !== null ? avg : '—';
}

// ===== INPUT EVENTS ===== //

// Recalculate average while typing
nota1Input.addEventListener('input', calcAvg);
nota2Input.addEventListener('input', calcAvg);

// Clamp values when input loses focus
nota1Input.addEventListener('blur', () => {
    clampGrade(nota1Input);
    calcAvg();
});

nota2Input.addEventListener('blur', () => {
    clampGrade(nota2Input);
    calcAvg();
});

// ===== BUTTON ACTIONS ===== //

// Close grade modal
document.getElementById('btnBack').addEventListener('click', closeGradeModal);

// Save grades button
document.getElementById('btnSaveNote').addEventListener('click', () => {
    const nota1Raw = nota1Input.value.trim();
    const nota2Raw = nota2Input.value.trim();

    if (
        (nota1Raw && !isValidGradeInput(nota1Input)) ||
        (nota2Raw && !isValidGradeInput(nota2Input))
    ) {
        calcAvg();
        showToast(
            'error',
            'Notas inválidas',
            'Digite apenas valores entre 0 e 10.'
        );
        return;
    }

    clampGrade(nota1Input);
    clampGrade(nota2Input);

    const avg = calculateAverageValue();

    if (avg === null) {
        showToast(
            'error',
            'Notas incompletas',
            'Preencha as duas notas para continuar.'
        );
        return;
    }

    saveHint.style.display = 'block';

    confirmStudent.textContent = gradeTitle.textContent;
    confirmAvg.textContent = `Média: ${avg}`;

    confirmOverlay.classList.add('show');
    confirmOverlay.setAttribute('aria-hidden', 'false');
});

// Cancel confirmation
document.getElementById('confirmCancel').addEventListener('click', closeConfirmModal);

// Confirm save action
document.getElementById('confirmSend').addEventListener('click', () => {
    const btn = document.getElementById('confirmSend');
    btn.disabled = true;
    btn.textContent = 'Enviando...';

    formEdit.submit();
});

// ===== OVERLAY CLICK HANDLERS ===== //

// Close grade modal when clicking outside
gradeOverlay.addEventListener('click', (e) => {
    if (e.target === gradeOverlay) closeGradeModal();
});

// Close confirmation modal when clicking outside
confirmOverlay.addEventListener('click', (e) => {
    if (e.target === confirmOverlay) closeConfirmModal();
});

// ===== TOAST NOTIFICATION ===== //

// Create and display toast message
function showToast(type = 'success', title = '', subtitle = '') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const iconSrc =
        type === 'error'
            ? contextPath + '/assets/error-icon.svg'
            : contextPath + '/assets/check-icon.svg';

    toast.innerHTML = `
        <img src="${iconSrc}" class="toast-icon" alt="">
        <div class="toast-text">
            <h4>${title}</h4>
            <p>${subtitle}</p>
        </div>
    `;

    toastWrap.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('hide');
    }, 2600);

    setTimeout(() => {
        toast.remove();
    }, 3200);
}

// ===== KEYBOARD SHORTCUTS ===== //

// Close modals using ESC key
document.addEventListener('keydown', (e) => {
    if (e.key !== 'Escape') return;

    if (confirmRecoveryOverlay.classList.contains('show')) {
        confirmRecoveryOverlay.classList.remove('show');
        confirmRecoveryOverlay.setAttribute('aria-hidden', 'true');
    } else if (recoveryOverlay.classList.contains('show')) {
        recoveryOverlay.classList.remove('show');
        recoveryOverlay.setAttribute('aria-hidden', 'true');
    } else if (confirmOverlay.classList.contains('show')) {
        closeConfirmModal();
    } else if (gradeOverlay.classList.contains('show')) {
        closeGradeModal();
    }
});

// ===== RECOVERY - ELEMENTS ===== //

const recoveryOverlay = document.getElementById('recoveryOverlay');
const recNota1 = document.getElementById('recNota1');
const recNota2 = document.getElementById('recNota2');
const recMedia = document.getElementById('recMedia');
const recInput = document.getElementById('recInput');
const recFinal = document.getElementById('recFinal');
const recoveryTitle = document.getElementById('recoveryTitle');
const recoveryEmail = document.getElementById('recoveryEmail');
const recoveryMat = document.getElementById('recoveryMat');

const recBackBtn = document.getElementById('recBack');
const recToConfirmBtn = document.getElementById('recToConfirm');

// Confirmation modal elements
const confirmRecoveryOverlay = document.getElementById('confirmRecoveryOverlay');
const confirmRecoveryStudent = document.getElementById('confirmRecoveryStudent');
const confirmRecoveryAvg = document.getElementById('confirmRecoveryAvg');
const confirmRecoveryCancel = document.getElementById('confirmRecoveryCancel');
const confirmRecoverySend = document.getElementById('confirmRecoverySend');

// Stores the current student row being edited
let currentRecoveryRow = null;

// ===== HELPER FUNCTIONS ===== //

function parseGradeVal(val) {
    if (val === undefined || val === null || val === '') return null;

    const raw = String(val).trim().replace(',', '.');
    if (raw === '') return null;

    const n = Number(raw);
    if (isNaN(n)) return null;

    return n;
}

function formatOneDecimal(v) {
    const n = Number(v);
    if (isNaN(n)) return '—';
    return n.toFixed(1).replace('.0', '');
}

function computeAverageFromNotes(n1, n2) {
    const v1 = parseGradeVal(n1);
    const v2 = parseGradeVal(n2);

    if (v1 === null || v2 === null) return null;

    const avg = (v1 + v2) / 2;
    return Number(avg.toFixed(1));
}

function computeFinalAverage(media, recovery) {
    const m = parseGradeVal(media);
    const r = parseGradeVal(recovery);

    if (m === null || r === null) return null;

    const avg = (m + r) / 2;
    return Number(avg.toFixed(1));
}

function isValidRecoveryInput(input) {
    const raw = input.value.trim().replace(',', '.');

    if (raw === '') return true;

    const value = Number(raw);
    return !isNaN(value) && value >= 0 && value <= 10;
}

function clampRecoveryInput(input) {
    const value = parseGradeVal(input.value);

    if (value === null) {
        input.value = '';
        input.setCustomValidity('');
        return;
    }

    let fixedValue = value;

    if (fixedValue < 0) fixedValue = 0;
    if (fixedValue > 10) fixedValue = 10;

    input.value = formatOneDecimal(fixedValue);
    input.setCustomValidity('');
}

function updateRecoveryFinal() {
    const raw = recInput.value.trim();

    if (raw === '') {
        recFinal.textContent = '—';
        recInput.setCustomValidity('');
        return;
    }

    if (!isValidRecoveryInput(recInput)) {
        recInput.setCustomValidity('Digite uma nota entre 0 e 10.');
        recInput.reportValidity();
        recFinal.textContent = '—';
        return;
    }

    recInput.setCustomValidity('');

    const mediaVal = parseGradeVal(recMedia.value);
    const recoveryVal = parseGradeVal(recInput.value);
    const finalAvg = computeFinalAverage(mediaVal, recoveryVal);

    recFinal.textContent = finalAvg !== null ? formatOneDecimal(finalAvg) : '—';
}

// ===== OPEN RECOVERY MODAL ===== //

// Detect click on recovery icon
document.addEventListener('click', (e) => {
    const recoveryIcon = e.target.closest('.recovery');
    if (!recoveryIcon || recoveryIcon.classList.contains('disabled')) return;

    const row = recoveryIcon.closest('.student-row');
    if (!row) return;

    openRecoveryModal(row);
});

function openRecoveryModal(row) {
    currentRecoveryRow = row;

    const nota1 = (row.dataset.n1 || '').replace(",", ".");
    const nota2 = (row.dataset.n2 || '').replace(",", ".");
    const enrollment = row.dataset.enrollment;

    recNota1.value = nota1 !== "-1.0" ? nota1 : '';
    recNota2.value = nota2 !== "-1.0" ? nota2 : '';
    document.getElementById("enrollmentRec").value = enrollment;

    let mediaVal = null;

    if (nota1 !== '' && nota2 !== '' && nota1 !== "-1.0" && nota2 !== "-1.0") {
        mediaVal = computeAverageFromNotes(nota1, nota2);
    } else if (row.dataset.average) {
        mediaVal = parseGradeVal(row.dataset.average);
    }

    recMedia.value = mediaVal !== null ? formatOneDecimal(mediaVal) : '';

    recInput.value = '';
    recInput.setCustomValidity('');
    recFinal.textContent = '—';

    recoveryTitle.textContent = row.querySelector('.nome')?.textContent.trim() || '';
    recoveryEmail.textContent = row.querySelector('.email')?.textContent.trim() || '';
    recoveryMat.textContent = row.querySelector('.matricula')?.textContent.trim() || '';

    recoveryOverlay.classList.add('show');
    recoveryOverlay.setAttribute('aria-hidden', 'false');

    setTimeout(() => recInput.focus(), 120);
}

// ===== RECOVERY INPUT EVENTS ===== //

recInput.addEventListener('input', updateRecoveryFinal);

recInput.addEventListener('blur', () => {
    clampRecoveryInput(recInput);
    updateRecoveryFinal();
});

// ===== FOOTER BUTTON ACTIONS ===== //

// Close recovery modal
recBackBtn.addEventListener('click', () => {
    recoveryOverlay.classList.remove('show');
    recoveryOverlay.setAttribute('aria-hidden', 'true');
});

// Open confirmation modal
recToConfirmBtn.addEventListener('click', () => {
    if (!recInput.value.trim()) {
        showToast(
            'error',
            'Digite a nota de recuperação',
            'Por favor, informe a nota para continuar.'
        );
        return;
    }

    if (!isValidRecoveryInput(recInput)) {
        recInput.setCustomValidity('Digite uma nota entre 0 e 10.');
        recInput.reportValidity();

        showToast(
            'error',
            'Nota inválida',
            'Digite apenas valores entre 0 e 10.'
        );
        return;
    }

    clampRecoveryInput(recInput);
    updateRecoveryFinal();

    const final = recFinal.textContent;

    if (!final || final === '—') {
        showToast(
            'error',
            'Média inválida',
            'Verifique as notas e tente novamente.'
        );
        return;
    }

    confirmRecoveryStudent.textContent = recoveryTitle.textContent;
    confirmRecoveryAvg.textContent = `Média final: ${final}`;
    confirmRecoveryOverlay.classList.add('show');
    confirmRecoveryOverlay.setAttribute('aria-hidden', 'false');
});

// ===== CONFIRM MODAL EVENTS ===== //

// Cancel confirmation
confirmRecoveryCancel.addEventListener('click', () => {
    confirmRecoveryOverlay.classList.remove('show');
    confirmRecoveryOverlay.setAttribute('aria-hidden', 'true');
});

// Close when clicking outside
confirmRecoveryOverlay.addEventListener('click', (e) => {
    if (e.target === confirmRecoveryOverlay) {
        confirmRecoveryOverlay.classList.remove('show');
        confirmRecoveryOverlay.setAttribute('aria-hidden', 'true');
    }
});

// ===== SAVE RECOVERY GRADE ===== //

confirmRecoverySend.addEventListener('click', () => {
    const btn = confirmRecoverySend;
    btn.disabled = true;
    btn.textContent = 'Salvando...';

    formEditRec.submit();
});

// ===== CLOSE RECOVERY MODAL ON OUTSIDE CLICK ===== //

recoveryOverlay.addEventListener('click', (e) => {
    if (e.target === recoveryOverlay) {
        recoveryOverlay.classList.remove('show');
        recoveryOverlay.setAttribute('aria-hidden', 'true');
    }
});

// =========================================================
// Apply UI behavior based on data-state
// (Business rules come from backend)
// =========================================================

function applyRowUIState(row) {
    if (!row) return;

    const state = row.dataset.state;

    const plusBtn = row.querySelector('.action-btn.plus');
    const recoveryIcon = row.querySelector('.recovery');

    plusBtn?.classList.remove('disabled');
    recoveryIcon?.classList.remove('disabled');

    switch (state) {
        case 'no-notes':
            recoveryIcon?.classList.add('disabled');
            break;

        case 'needs-recovery':
            plusBtn?.classList.add('disabled');
            break;

        case 'complete':
            plusBtn?.classList.add('disabled');
            recoveryIcon?.classList.add('disabled');
            break;
    }
}

// Apply state to all rows on page load
document.querySelectorAll('.student-row').forEach(row => {
    applyRowUIState(row);
});

window.showToast = showToast;

// ===== Filter situation ===== //

const buttonStatusFilter = document.getElementById("buttonStatusFilter");
const popupFilter = document.getElementById("popupFilter");

if (buttonStatusFilter && popupFilter) {
    buttonStatusFilter.addEventListener("click", (e) => {
        e.stopPropagation();

        const rect = buttonStatusFilter.getBoundingClientRect();

        popupFilter.style.left = rect.left + "px";
        popupFilter.style.top = rect.bottom + window.scrollY + 10 + "px";

        popupFilter.classList.toggle("show");
    });

    document.addEventListener("click", () => {
        popupFilter.classList.remove("show");
    });

    popupFilter.addEventListener("click", (e) => {
        const item = e.target.closest(".popup-filter-item");
        if (!item) return;

        if (item.classList.contains("clear")) {
            window.location = `reportCardStudentsList?grade=${grade}`;
            return;
        }

        const filter = item.dataset.filter;
        window.location = `studentsFilterDTO?situation=${filter}&page=reportcard-students-list`;
    });
}