const openBtn = document.getElementById("openLogout");
const overlay = document.getElementById("logoutOverlay");
const cancelBtn = document.getElementById("cancelLogout");

openBtn.addEventListener("click", () => {
    overlay.classList.add("show");
});

cancelBtn.addEventListener("click", () => {
    overlay.classList.remove("show");
});

overlay.addEventListener("click", (e) => {
    if (e.target === overlay) {
        overlay.classList.remove("show");
    }
});