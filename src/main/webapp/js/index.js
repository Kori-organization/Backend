const passwordInput = document.getElementById('password');
const togglePassword = document.getElementById('togglePassword');

togglePassword.addEventListener('click', () => {
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        togglePassword.src = 'assets/eye.svg';
    } else {
        passwordInput.type = 'password';
        togglePassword.src = 'assets/eye-off.svg';
    }
});