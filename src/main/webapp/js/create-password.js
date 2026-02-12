const toggles = document.querySelectorAll('.toggle-password');

toggles.forEach(toggle => {
    toggle.addEventListener('click', () => {
        const input = document.getElementById(toggle.dataset.target);

        if (input.type === 'password') {
            input.type = 'text';
            toggle.src = '../../Assets/eye.svg';
        } else {
            input.type = 'password';
            toggle.src = '../../Assets/eye-off.svg';
        }
    });
});