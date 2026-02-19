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

const form = document.getElementById("loginForm");

const password = document.getElementById("password");

const passwordError = document.getElementById("passwordError");

form.addEventListener("submit", function(e)
{
    e.preventDefault();

    let valid = true;

    // reset
    passwordError.style.display = "none";

    password.classList.remove("input-error");

    // PASSWORD VALIDATION
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;

    if(!regex.test(password.value))
    {
        passwordError.style.display = "block";
        password.classList.add("input-error");
        valid = false;
    }

    if(valid) {
        form.submit();
    }

});