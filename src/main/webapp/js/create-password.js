const toggles = document.querySelectorAll('.toggle-password');

toggles.forEach(toggle => {
    toggle.addEventListener('click', () => {
        const input = document.getElementById(toggle.dataset.target);

        if (input.type === 'password') {
            input.type = 'text';
            toggle.src = contextPath + '/assets/eye.svg';
        } else {
            input.type = 'password';
            toggle.src = contextPath + '/assets/eye-off.svg';
        }
    });
});