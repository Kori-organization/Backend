// Save button
const toastWrap = document.getElementById('toastWrap');

// Init
document.addEventListener('DOMContentLoaded', () => {
    calculateAll();
    attachInputListeners();
});

// Toast
const toast = document.getElementById('toast');
let toastTimeout;

function createToast(title, message = '', type = 'success') {
    if (!toastWrap) return;

    const toast = document.createElement('div');
    toast.className = `toastDown ${type === 'error' ? 'error' : 'success'}`;

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

function showToast(message) {
    if (!toast) return;

    toast.textContent = message;
    toast.classList.add('show');

    clearTimeout(toastTimeout);
    toastTimeout = setTimeout(() => {
        toast.classList.remove('show');
    }, 2500);
}

// Input validation (0–10)
function validateGrade(input) {
    input.addEventListener('input', () => {
        if (input.value === '') return;

        const value = Number(input.value);
        if (Number.isNaN(value)) return;

        if (value < 0 || value > 10) {
            input.value = '';
            showToast('Número inválido (0 até 10)');
            calculateAll();
        } else {
            input.classList.remove('warning');
        }
    });
}

// Apply warning borders
function updateWarnings() {
    const n1 = document.querySelectorAll('.n1');
    const n2 = document.querySelectorAll('.n2');
    const rec = document.querySelectorAll('.rec');

    n1.forEach((_, i) => {
        const g1 = n1[i];
        const g2 = n2[i];
        const r = rec[i];

        // Grades warning individual
        if (g1.value === '') {
            g1.classList.add('warning');
        } else {
            g1.classList.remove('warning');
        }

        if (g2.value === '') {
            g2.classList.add('warning');
        } else {
            g2.classList.remove('warning');
        }

        // Recovery warning (only if enabled)
        if (!r.disabled && r.value === '') {
            r.classList.add('warning');
        } else {
            r.classList.remove('warning');
        }
    });
}

// Listeners
function attachInputListeners() {
    document.querySelectorAll('.nota').forEach(input => {
        validateGrade(input);

        input.addEventListener('input', updateWarnings);
        input.addEventListener('blur', calculateAll);
    });
}

// Calculation
function calculateAll() {
    const n1 = document.querySelectorAll('.n1');
    const n2 = document.querySelectorAll('.n2');
    const rec = document.querySelectorAll('.rec');
    const medias = document.querySelectorAll('.media-final');
    const status = document.querySelectorAll('.status');
    let approvedOverall = true;

    n1.forEach((_, i) => {
        const grade1 = Number(n1[i].value) || 0;
        const grade2 = Number(n2[i].value) || 0;
        const avg = (grade1 + grade2) / 2;
        let finalAvg = avg;

        if (avg < 7) {
            rec[i].disabled = false;
            const recovery = Number(rec[i].value) || 0;
            finalAvg = (avg + recovery) / 2;
        }
        else {
            rec[i].disabled = true;
            rec[i].value = '';
        }

        medias[i].textContent = finalAvg.toFixed(1);

        if (finalAvg >= 7) {
            status[i].textContent = 'Aprovado';
            status[i].classList.add('green');
            status[i].classList.remove('red');
        }
        else {
            status[i].textContent = 'Reprovado';
            status[i].classList.add('red');
            status[i].classList.remove('green');
            approvedOverall = false;
        }
    });
    document.querySelector('.final-status span').textContent =
        `Situação final: ${approvedOverall ? 'Aprovado' : 'Reprovado'}.`;

    updateWarnings();
}

// Init
document.addEventListener('DOMContentLoaded', calculateAll);

window.createToast = createToast;