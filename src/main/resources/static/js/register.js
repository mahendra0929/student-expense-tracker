document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("registerForm").addEventListener("submit", registerUser);
});

async function registerUser(event) {
    event.preventDefault();

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    if (!name || !email || !password) {
        showMessage("formMessage", "All fields are required.", "error");
        return;
    }

    if (password.length < 6) {
        showMessage("formMessage", "Password must be at least 6 characters long.", "error");
        return;
    }

    try {
        const response = await authRequest("/register", {
            method: "POST",
            body: JSON.stringify({ name, email, password })
        });
        showMessage("formMessage", response.message);
        setTimeout(() => {
            window.location.href = "/index.html";
        }, 600);
    } catch (error) {
        showMessage("formMessage", error.message, "error");
    }
}
