// ===== DOM ELEMENT REFERENCES =====

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


// ===== GLOBAL CLICK HANDLER =====

// Detect clicks on "add grade" buttons
document.addEventListener('click', (e) => {
    const plus = e.target.closest('.action-btn.plus');
    if (!plus) return;

    const row = plus.closest('.student-row');
    openGradeModal(row);
});


// ===== MODAL CONTROL FUNCTIONS =====

// Open grade modal and populate student data
function openGradeModal(row) {
    currentRow = row;

    gradeTitle.textContent = row.querySelector('.nome').textContent.trim();
    gradeEmail.textContent = row.querySelector('.email').textContent.trim();
    gradeMat.textContent = row.querySelector('.matricula').textContent.trim();

    // Reset inputs and UI
    nota1Input.value = '';
    nota2Input.value = '';
    avgValue.textContent = '—';
    saveHint.style.display = 'none';

    // Show modal
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


// ===== GRADE UTILITIES =====

// Parse numeric grade value safely
function getGradeValue(input) {
    if (!input.value) return 0;

    const value = parseFloat(input.value.replace(',', '.'));
    return isNaN(value) ? 0 : value;
}

// Clamp grade value between 0 and 10
function clampGrade(input) {
    let value = getGradeValue(input);

    if (value > 10) value = 10;
    if (value < 0) value = 0;

    // Format value with one decimal (remove .0)
    input.value = value === 0 ? '' : value.toFixed(1).replace('.0', '');
}

// Calculate average value
function calculateAverageValue() {
    const v1 = getGradeValue(nota1Input);
    const v2 = getGradeValue(nota2Input);
    return ((v1 + v2) / 2).toFixed(1);
}

// Update average display dynamically
function calcAvg() {
    if (!nota1Input.value && !nota2Input.value) {
        avgValue.textContent = '—';
        return;
    }

    avgValue.textContent = calculateAverageValue();
}


// ===== INPUT EVENTS =====

// Recalculate average while typing
nota1Input.addEventListener('input', calcAvg);
nota2Input.addEventListener('input', calcAvg);

// Clamp values when input loses focus
nota1Input.addEventListener('blur', () => clampGrade(nota1Input));
nota2Input.addEventListener('blur', () => clampGrade(nota2Input));


// ===== BUTTON ACTIONS =====

// Close grade modal
document.getElementById('btnBack').addEventListener('click', closeGradeModal);

// Save grades button
document.getElementById('btnSaveNote').addEventListener('click', () => {
    clampGrade(nota1Input);
    clampGrade(nota2Input);

    const avg = calculateAverageValue();

    // Show admin warning
    saveHint.style.display = 'block';

    // Populate confirmation modal
    confirmStudent.textContent = gradeTitle.textContent;
    confirmAvg.textContent = `Média: ${avg}`;

    // Show confirmation modal
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

    // Simulate async request
    setTimeout(() => {
        const success = Math.random() < 0.88;

        if (success) {
            showToast('success', 'Nota salva com sucesso.', 'A nota foi salva com êxito.');

            // Store average on row dataset
            if (currentRow) {
                currentRow.dataset.average = calculateAverageValue();
                currentRow.style.boxShadow = '0 10px 28px rgba(2,6,23,0.06)';
            }

            closeConfirmModal();
            closeGradeModal();
        } else {
            showToast(
                'error',
                'Não foi possível salvar a nota.',
                'Verifique sua conexão e tente novamente.'
            );
            closeConfirmModal();
        }

        // Reset button state
        btn.disabled = false;
        btn.textContent = 'Tem certeza?';
    }, 720);
});


// ===== OVERLAY CLICK HANDLERS =====

// Close grade modal when clicking outside
gradeOverlay.addEventListener('click', (e) => {
    if (e.target === gradeOverlay) closeGradeModal();
});

// Close confirmation modal when clicking outside
confirmOverlay.addEventListener('click', (e) => {
    if (e.target === confirmOverlay) closeConfirmModal();
});


// ===== TOAST NOTIFICATION =====

// Create and display toast message
function showToast(type = 'success', title = '', subtitle = '') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const iconSrc =
        type === 'error'
            ? '../Assets/error-icon.svg'
            : '../Assets/check-icon.svg';

    toast.innerHTML = `
        <img src="${iconSrc}" class="toast-icon" alt="">
        <div class="toast-text">
            <h4>${title}</h4>
            <p>${subtitle}</p>
        </div>
    `;

    toastWrap.appendChild(toast);

    // Auto remove
    setTimeout(() => {
        toast.classList.add('hide');
    }, 2600);

    setTimeout(() => {
        toast.remove();
    }, 3200);
}


// ===== KEYBOARD SHORTCUTS =====

// Close modals using ESC key
document.addEventListener('keydown', (e) => {
    if (e.key !== 'Escape') return;

    if (confirmOverlay.classList.contains('show')) closeConfirmModal();
    else if (gradeOverlay.classList.contains('show')) closeGradeModal();
});

// ===== RECOVERY - ELEMENTS =====
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


// ===== HELPER FUNCTIONS =====

/**
 * Converts a value to number safely.
 * Does NOT modify the original input field.
 * Returns 0 if invalid.
 */
function parseGradeVal(val) {
    if (val === undefined || val === null || val === '') return 0;
    const n = Number(val);
    if (isNaN(n)) return 0;
    return n;
}


/**
 * Formats number with one decimal place when necessary.
 * Removes trailing ".0"
 * Returns dash if invalid.
 */
function formatOneDecimal(v) {
    const n = Number(v);
    if (isNaN(n)) return '—';
    const s = n.toFixed(1);
    return s.replace('.0', '');
}


/**
 * Calculates average between two grades
 */
function computeAverageFromNotes(n1, n2) {
    const avg = (parseGradeVal(n1) + parseGradeVal(n2)) / 2;
    return Number(avg.toFixed(1));
}


/**
 * Calculates final average between media and recovery
 */
function computeFinalAverage(media, recovery) {
    const avg = (parseGradeVal(media) + parseGradeVal(recovery)) / 2;
    return Number(avg.toFixed(1));
}



// ===== OPEN RECOVERY MODAL =====

// Detect click on recovery icon
document.addEventListener('click', (e) => {
    const recoveryIcon = e.target.closest('.recovery');
    if (!recoveryIcon) return;
    const row = recoveryIcon.closest('.student-row');
    if (!row) return;
    openRecoveryModal(row);
});



/**
 * Opens recovery modal and fills student data
 */
function openRecoveryModal(row) {
    currentRecoveryRow = row;

    // Get stored grades if available
    const savedN1 = row.dataset.nota1 ?? '';
    const savedN2 = row.dataset.nota2 ?? '';

    // Fill grade inputs
    recNota1.value = savedN1;
    recNota2.value = savedN2;

    // Calculate media if possible
    let mediaVal;

    if (savedN1 || savedN2)
        mediaVal = computeAverageFromNotes(savedN1, savedN2);
    else if (row.dataset.average)
        mediaVal = parseGradeVal(row.dataset.average);
    else
        mediaVal = '';

    // Fill media field
    recMedia.value = mediaVal !== '' ? formatOneDecimal(mediaVal) : '';

    // Reset recovery input and final display
    recInput.value = '';
    recFinal.textContent = '—';

    // Fill student info
    recoveryTitle.textContent =
        row.querySelector('.nome')?.textContent.trim() || '';

    recoveryEmail.textContent =
        row.querySelector('.email')?.textContent.trim() || '';

    recoveryMat.textContent =
        row.querySelector('.matricula')?.textContent.trim() || '';

    // Show modal
    recoveryOverlay.classList.add('show');
    recoveryOverlay.setAttribute('aria-hidden', 'false');

    // Focus input
    setTimeout(() => recInput.focus(), 120);
}



// ===== RECOVERY INPUT EVENTS =====

/**
 * Updates final average while typing
 * Does NOT modify the input value
 */
recInput.addEventListener('input', () => {
    const raw = recInput.value;
    // Reset if empty
    if (raw === '') {
        recFinal.textContent = '—';
        return;
    }
    const recoveryVal = parseGradeVal(raw);
    const mediaVal = parseGradeVal(recMedia.value);
    const finalAvg = computeFinalAverage(mediaVal, recoveryVal);
    recFinal.textContent = formatOneDecimal(finalAvg);
});


/**
 * Formats recovery value only when leaving input
 */
recInput.addEventListener('blur', () => {
    if (recInput.value === '') return;
    let val = parseGradeVal(recInput.value);

    // Clamp between 0 and 10
    if (val < 0) val = 0;
    if (val > 10) val = 10;

    // Apply formatted value
    recInput.value = formatOneDecimal(val);
    const mediaVal = parseGradeVal(recMedia.value);
    const finalAvg = computeFinalAverage(mediaVal, val);
    recFinal.textContent = formatOneDecimal(finalAvg);
});



// ===== FOOTER BUTTON ACTIONS =====

// Close recovery modal
recBackBtn.addEventListener('click', () => {
    recoveryOverlay.classList.remove('show');
    recoveryOverlay.setAttribute('aria-hidden', 'true');
});



// Open confirmation modal
recToConfirmBtn.addEventListener('click', () => {
    if (!recInput.value) {
        showToast(
            'error',
            'Digite a nota de recuperação',
            'Por favor, informe a nota para continuar'
        );
        return;
    }

    const final = recFinal.textContent;

    if (!final || final === '—') {
        showToast(
            'error',
            'Média inválida',
            'Verifique as notas e tente novamente'
        );
        return;
    }
    confirmRecoveryStudent.textContent = recoveryTitle.textContent;
    confirmRecoveryAvg.textContent = `Média final: ${final}`;
    confirmRecoveryOverlay.classList.add('show');
    confirmRecoveryOverlay.setAttribute('aria-hidden', 'false');
});



// ===== CONFIRM MODAL EVENTS =====

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

// ===== SAVE RECOVERY GRADE =====
confirmRecoverySend.addEventListener('click', () => {
    const btn = confirmRecoverySend;
    btn.disabled = true;
    btn.textContent = 'Salvando...';

    setTimeout(() => {
        const success = Math.random() < 0.92;
        if (success) {
            if (currentRecoveryRow) {
                currentRecoveryRow.dataset.recovery = recInput.value;
                currentRecoveryRow.dataset.averageFinal = recFinal.textContent;
                if (recNota1.value)
                    currentRecoveryRow.dataset.nota1 = recNota1.value;
                if (recNota2.value)
                    currentRecoveryRow.dataset.nota2 = recNota2.value;
                currentRecoveryRow.style.boxShadow =
                    '0 10px 28px rgba(2,6,23,0.06)';
            }
            showToast(
                'success',
                'Recuperação salva',
                'A nota foi registrada com sucesso'
            );
            confirmRecoveryOverlay.classList.remove('show');
            recoveryOverlay.classList.remove('show');
        }
        else {
            showToast(
                'error',
                'Erro ao salvar',
                'Verifique sua conexão e tente novamente'
            );
            confirmRecoveryOverlay.classList.remove('show');
        }

        btn.disabled = false;
        btn.textContent = 'Salvar';
    }, 720);
});

// ===== CLOSE RECOVERY MODAL ON OUTSIDE CLICK =====
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

    // Reset states
    plusBtn?.classList.remove('disabled');
    recoveryIcon?.classList.remove('disabled');

    switch (state) {
        case 'no-notes':
            // User can add grades, recovery is disabled
            recoveryIcon?.classList.add('disabled');
            break;

        case 'needs-recovery':
            // Grades locked, recovery enabled
            plusBtn?.classList.add('disabled');
            break;

        case 'complete':
            // Everything locked, row highlighted
            plusBtn?.classList.add('disabled');
            recoveryIcon?.classList.add('disabled');
            break;
    }
}

// Apply state to all rows on page load
document.querySelectorAll('.student-row').forEach(row => {
    applyRowUIState(row);
});