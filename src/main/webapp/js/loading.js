// loading.js
document.addEventListener("DOMContentLoaded", () => {
    function checkStatus() {
        fetch("status")
            .then((res) => res.text())
            .then((data) => {
                if (data === "READY") {
                    window.location.href = "enter";
                } else {
                    setTimeout(checkStatus, 1000);
                }
            })
            .catch(() => {
                setTimeout(checkStatus, 1500);
            });
    }
    checkStatus();

    history.pushState(null, "", location.href='enter');
    window.addEventListener("popstate", () => {
        history.go(1);
    });

    const titleEl = document.getElementById("title");

    function animateTitle() {
        if (!titleEl) return;

        let next = "";
        switch (titleEl.innerHTML) {
            case "Kori - Carregando":
                next = "Kori - Carregando.";
                break;
            case "Kori - Carregando.":
                next = "Kori - Carregando..";
                break;
            case "Kori - Carregando..":
                next = "Kori - Carregando...";
                break;
            case "Kori - Carregando...":
            default:
                next = "Kori - Carregando";
                break;
        }
        titleEl.innerHTML = next;
        setTimeout(animateTitle, 500);
    }

    animateTitle();
});