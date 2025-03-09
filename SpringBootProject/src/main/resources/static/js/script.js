document.addEventListener("DOMContentLoaded", function checkPasswordMatch() {
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
});

document.addEventListener("DOMContentLoaded", function () {
    const backToTopButton = document.getElementById("backToTop");

    window.addEventListener("scroll", function () {
        if (window.scrollY > 300) {
            backToTopButton.style.display = "block";
        } else {
            backToTopButton.style.display = "none";
        }
    });

    backToTopButton.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });
});
