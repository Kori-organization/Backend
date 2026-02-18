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