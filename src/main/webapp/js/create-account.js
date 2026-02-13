const password = document.getElementById('password');
const togglePassword = document.getElementById('togglePassword');

togglePassword.addEventListener('click', () => {
    password.type = password.type === 'password' ? 'text' : 'password';
    togglePassword.src = password.type === 'password'
        ? contextPath + '/assets/eye-off.svg'
        : contextPath + '/assets/eye.svg';
});