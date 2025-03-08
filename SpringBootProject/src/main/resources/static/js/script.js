function checkPasswordMatch() {
    let password = document.getElementById('password').value;
    let confirmPassword = document.getElementById('password_confirm').value;
    let errorDiv = document.getElementById('passwordError');
    let submitButton = document.getElementById('submitButton');

    if (password !== confirmPassword) {
        errorDiv.style.display = "block";
        submitButton.disabled = true;
    } else {
        errorDiv.style.display = "none";
        submitButton.disabled = false;
    }
}