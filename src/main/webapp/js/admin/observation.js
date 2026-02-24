// ===== COPY FUNCTION =====

const botoesCopiar = document.querySelectorAll(".copy-btn");
const toastCopy = document.getElementById("copyToast");
const toastWrap = document.getElementById('toastWrapTeacher');

botoesCopiar.forEach(botao => {

    botao.addEventListener("click", () => {

        const card = botao.closest(".obs-card");
        const texto = card.querySelector("p").innerText;

        navigator.clipboard.writeText(texto)
            .then(() => {

                toastCopy.classList.add("show");

                setTimeout(() => {
                    toastCopy.classList.remove("show");
                }, 2000);

            });

    });

});

// =========================
// TOAST
// =========================
function createToast(title, message = '', type = 'success') {

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


document.addEventListener('DOMContentLoaded', () => {

    const deleteButtons = document.querySelectorAll('.delete-btn');

    const confirmDeleteOverlay = document.getElementById('confirmDeleteOverlay');
    const confirmDeleteBtn = document.getElementById('confirmDelete');
    const confirmCancelBtn = document.getElementById('confirmCancel');

    let cardToDelete = null;

    // =========================
    // OPEN MODAL
    // =========================
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            cardToDelete = btn.closest('.obs-card');
            confirmDeleteOverlay.classList.add('show');
        });
    });

    // =========================
    // CANCEL
    // =========================
    confirmCancelBtn.addEventListener('click', () => {
        confirmDeleteOverlay.classList.remove('show');
        cardToDelete = null;
    });

    // =========================
    // CONFIRMATION
    // =========================
    confirmDeleteBtn.addEventListener('click', () => {
        if (!cardToDelete) return;
        // DO NOT remove the card


        const id = cardToDelete.querySelector('.id')?.value.trim();
        window.location = `deleteObservation?id=${id}`
    });

    // =========================
    // ESC MODAL
    // =========================
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            confirmDeleteOverlay.classList.remove('show');
        }
    });

});

window.createToast = createToast;