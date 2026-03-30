document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loginForm").addEventListener("submit", loginUser);
    initializeContactModal();
});

async function loginUser(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    if (!email || !password) {
        showMessage("formMessage", "Email and password are required.", "error");
        return;
    }

    try {
        const response = await authRequest("/login", {
            method: "POST",
            body: JSON.stringify({ email, password })
        });
        showMessage("formMessage", response.message);
        setTimeout(() => {
            window.location.href = "/index.html";
        }, 600);
    } catch (error) {
        showMessage("formMessage", error.message, "error");
    }
}


function initializeContactModal() {
    const contactToggle = document.getElementById("contactToggle");
    const contactModal = document.getElementById("contactModal");
    const closeButtons = document.querySelectorAll("[data-close-contact]");
    const closeButton = document.getElementById("contactCloseButton");

    if (!contactToggle || !contactModal) {
        return;
    }

    const openModal = () => {
        contactModal.classList.add("open");
        contactModal.setAttribute("aria-hidden", "false");
        document.body.classList.add("contact-open");
        if (closeButton) {
            closeButton.focus();
        }
    };

    const closeModal = () => {
        contactModal.classList.remove("open");
        contactModal.setAttribute("aria-hidden", "true");
        document.body.classList.remove("contact-open");
        contactToggle.focus();
    };

    contactToggle.addEventListener("click", openModal);
    closeButtons.forEach((button) => button.addEventListener("click", closeModal));

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && contactModal.classList.contains("open")) {
            closeModal();
        }
    });
}
