// Select all copy buttons
const botoesCopiar = document.querySelectorAll(".copy-btn");

// Select toast element
const toast = document.getElementById("copyToast");

botoesCopiar.forEach(botao => {
    botao.addEventListener("click", () => {

        // Get parent card
        const card = botao.closest(".obs-card");

        // Get paragraph text
        const texto = card.querySelector("p").innerText;

        // Copy text
        navigator.clipboard.writeText(texto)
            .then(() => {

                // Show toast
                toast.classList.add("show");

                // Hide toast after 2 seconds
                setTimeout(() => {
                    toast.classList.remove("show");
                }, 2000);

            })
            .catch(err => {
                console.error("Copy error:", err);
            });

    });
});

const observationOverlay = document.getElementById("observationOverlay");
const confirmObservationOverlay = document.getElementById("confirmObservationOverlay");

const btnCancelObservation = document.getElementById("btnCancelObservation");
const btnSaveObservation = document.getElementById("btnSaveObservation");

const confirmObsCancel = document.getElementById("confirmObsCancel");
const confirmObsSend = document.getElementById("confirmObsSend");

const observationInput = document.getElementById("observationInput");

const toastWrap = document.getElementById("toastWrap");


// OPEN MODAL
document.querySelector(".obs-card.p2").addEventListener("click", () => {

    observationOverlay.classList.add("show");

});


// CLOSE MODAL
btnCancelObservation.addEventListener("click", () => {

    observationOverlay.classList.remove("show");

});


// OPEN CONFIRM
btnSaveObservation.addEventListener("click", () => {

    confirmObservationOverlay.classList.add("show");

});


// CANCEL CONFIRM
confirmObsCancel.addEventListener("click", () => {

    confirmObservationOverlay.classList.remove("show");

});


// CONFIRM SAVE
confirmObsSend.addEventListener("click", () => {

    confirmObservationOverlay.classList.remove("show");
    observationOverlay.classList.remove("show");

    showToast(
    "success",
    "Observação salva com sucesso",
    "A observação foi registrada com êxito."
);

});


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
            ${subtitle ? `<p>${subtitle}</p>` : ''}
        </div>
    `;

    toastWrap.appendChild(toast);

    // tempo total visível
    const AUTO_HIDE_MS = 4000;

    setTimeout(() => {

        toast.classList.add('hide');

        setTimeout(() => {
            toast.remove();
        }, 350);

    }, AUTO_HIDE_MS);

}