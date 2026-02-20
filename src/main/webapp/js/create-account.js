const password = document.getElementById('password');
const togglePassword = document.getElementById('togglePassword');

togglePassword.addEventListener('click', () => {
    password.type = password.type === 'password' ? 'text' : 'password';
    togglePassword.src = password.type === 'password'
        ? contextPath + '/assets/eye-off.svg'
        : contextPath + '/assets/eye.svg';
});

const form = document.getElementById("loginForm");
const passwordError = document.getElementById("passwordError");

form.addEventListener("submit", function(e)
{
    e.preventDefault();
    let valid = true;

    // Reset
    passwordError.style.display = "none";
    password.classList.remove("input-error");

    // Password validation
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;
    if(!regex.test(password.value)) {
        passwordError.style.display = "block";
        password.classList.add("input-error");
        valid = false;
    }

    if(valid) {
        form.submit();
    }
});