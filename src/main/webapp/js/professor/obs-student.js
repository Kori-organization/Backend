// Select all copy buttons
const botoesCopiar = document.querySelectorAll(".copy-btn");

// Select toast element
const toast = document.getElementById("copyToast");

const formAddObservation = document.getElementById("formAddObservation");

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

observationOverlay.addEventListener("click", (e) => {
    if (e.target === observationOverlay) {
        observationOverlay.classList.remove("show");
    }
});

confirmObservationOverlay.addEventListener("click", (e) => {
    if (e.target === confirmObservationOverlay) {
        confirmObservationOverlay.classList.remove("show");
    }
});


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
    const text = observationInput.value.split(" ");
    let wordLong = false;
    for (let word of text) {
        if (word.length > 30) {
            wordLong = true;
            break;
        }
    }

    if (!wordLong) {
        confirmObservationOverlay.classList.add("show");
    } else {
        showToast("error", "Limite de caracteres por palavra excedido", "Cada palavra pode ter no máximo 30 caracteres. Ajuste o texto para continuar.");
    }

});


// CANCEL CONFIRM
confirmObsCancel.addEventListener("click", () => {

    confirmObservationOverlay.classList.remove("show");

});


// CONFIRM SAVE
confirmObsSend.addEventListener("click", () => {
    formAddObservation.submit();
});


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
            ${subtitle ? `<p>${subtitle}</p>` : ''}
        </div>
    `;

    toastWrap.appendChild(toast);

    const AUTO_HIDE_MS = 4000;

    setTimeout(() => {

        toast.classList.add('hide');

        setTimeout(() => {
            toast.remove();
        }, 350);

    }, AUTO_HIDE_MS);

}

