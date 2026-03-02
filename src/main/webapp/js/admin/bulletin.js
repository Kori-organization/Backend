// Save button
const toastWrap = document.getElementById('toastWrap');

document.addEventListener('DOMContentLoaded', () => {
    attachInputListeners();
    calculateAll();
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

        g1.classList.toggle('warning', g1.value === '');
        g2.classList.toggle('warning', g2.value === '');

        // Recovery warning (only if enabled)
        r.classList.toggle('warning', !r.disabled && r.value === '');
    });
}

function attachInputListeners() {
    document.querySelectorAll('.nota').forEach(input => {
        validateGrade(input);

        input.addEventListener('blur', () => {
            updateWarnings();
            calculateAll();
        });
    });
}

function calculateAll() {
    const n1 = document.querySelectorAll('.n1');
    const n2 = document.querySelectorAll('.n2');
    const rec = document.querySelectorAll('.rec');
    const medias = document.querySelectorAll('.media-final');
    const status = document.querySelectorAll('.status');

    n1.forEach((_, i) => {
        const g1 = n1[i].value === '' ? null : Number(n1[i].value);
        const g2 = n2[i].value === '' ? null : Number(n2[i].value);
        const r  = rec[i].value === '' ? null : Number(rec[i].value);

        let finalAvg = null;
        let needRec = false;

        if (g1 !== null && g2 !== null) {
            const avg = (g1 + g2) / 2;

            if (avg < 7) {
                if (r !== null) {
                    finalAvg = (avg + r) / 2;
                } else {
                    needRec = true;
                }
            } else {
                finalAvg = avg;
            }
        }

        medias[i].textContent = finalAvg === null ? '-' : finalAvg.toFixed(1);

        status[i].classList.remove('red', 'green', 'yellow');

        if (g1 === null || g2 === null) {
            status[i].textContent = "-";
            rec[i].disabled = true;
        } else if (needRec) {
            status[i].textContent = "Recuperação";
            rec[i].disabled = false;
            status[i].classList.add('yellow');
        } else if (finalAvg >= 7) {
            status[i].textContent = "Aprovado";
            rec[i].disabled = true;
            status[i].classList.add('green');
        } else {
            status[i].textContent = "Reprovado";
            rec[i].disabled = true;
            status[i].classList.add('red');
        }
        
        if (rec[i].value !== '') {
            rec[i].disabled = false;
        }
    });

    const statusList = [...status].map(s => s.textContent.trim());
    let finalSituation = "";

    if (statusList.includes("-")) finalSituation = "Em andamento";
    else if (statusList.includes("Recuperação")) finalSituation = "Em andamento";
    else if (statusList.includes("Reprovado")) finalSituation = "Reprovado";
    else if (statusList.every(s => s === "Aprovado")) finalSituation = "Aprovado";

    document.querySelector('.final-status span').textContent =
        `Situação final: ${finalSituation}.`;

    updateWarnings();
}

document.getElementById('bulletinForm').addEventListener('submit', () => {
    document.querySelectorAll('.rec').forEach(input => {
        input.disabled = false;
    });
});

window.createToast = createToast;