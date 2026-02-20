const form = document.getElementById("createPasswordForm");

const newPassword = document.getElementById("newPassword");
const confirmNewPassword = document.getElementById("confirmNewPassword");

const newPasswordError = document.getElementById("newPasswordError");
const confirmPasswordError = document.getElementById("confirmPasswordError");


// Show / hide password
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


// Validation
form.addEventListener("submit", function(e)
{
    e.preventDefault();
    let valid = true;

    // Reset errors
    newPasswordError.style.display = "none";
    confirmPasswordError.style.display = "none";

    newPassword.classList.remove("input-error");
    confirmNewPassword.classList.remove("input-error");


    // Password strength
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/;
    if(!regex.test(newPassword.value))
    {
        newPasswordError.style.display = "block";
        newPassword.classList.add("input-error");
        valid = false;
    }

    // Confirm password
    if(newPassword.value !== confirmNewPassword.value)
    {
        confirmPasswordError.style.display = "block";
        confirmNewPassword.classList.add("input-error");
        valid = false;
    }

    if(valid)
    {
        // Sucess
        form.submit();
    }
});