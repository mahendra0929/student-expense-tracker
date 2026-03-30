const API_BASE_URL = "/api";
const AUTH_BASE_URL = "/auth";
const AUTH_PAGES = new Set(["login", "register"]);
let authReadyPromise = Promise.resolve(null);

function formatCurrency(value) {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        minimumFractionDigits: 2
    }).format(value || 0);
}

function formatCategory(category) {
    if (!category) {
        return "";
    }
    return category.charAt(0) + category.slice(1).toLowerCase();
}

function showMessage(elementId, message, type = "success") {
    const element = document.getElementById(elementId);
    if (!element) {
        return;
    }
    element.textContent = message;
    element.className = `form-message ${type}`;
}

async function parseApiResponse(response) {
    const contentType = response.headers.get("content-type") || "";
    const payload = contentType.includes("application/json") ? await response.json() : null;

    if (!response.ok) {
        const validationErrors = payload?.validationErrors;
        if (validationErrors) {
            throw new Error(Object.values(validationErrors).join(", "));
        }
        const errorMessage = payload?.message || "Something went wrong";
        if (response.status === 401 && !isAuthPage()) {
            redirectToLogin();
        }
        throw new Error(errorMessage);
    }

    return payload;
}

async function request(url, options = {}) {
    const response = await fetch(url, {
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    return parseApiResponse(response);
}

async function apiRequest(endpoint, options = {}) {
    return request(`${API_BASE_URL}${endpoint}`, options);
}

async function authRequest(endpoint, options = {}) {
    return request(`${AUTH_BASE_URL}${endpoint}`, options);
}

function setDefaultDate(inputId) {
    const input = document.getElementById(inputId);
    if (input && !input.value) {
        input.value = new Date().toISOString().split("T")[0];
    }
}

function setupThemeToggle() {
    const toggle = document.getElementById("themeToggle");
    const savedTheme = localStorage.getItem("expense-tracker-theme");

    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }

    if (toggle) {
        toggle.addEventListener("click", () => {
            document.body.classList.toggle("dark-mode");
            const theme = document.body.classList.contains("dark-mode") ? "dark" : "light";
            localStorage.setItem("expense-tracker-theme", theme);
        });
    }
}

function isAuthPage() {
    return AUTH_PAGES.has(document.body.dataset.page);
}

function redirectToLogin() {
    window.location.href = "/login.html";
}

function updateCurrentUser(user) {
    document.querySelectorAll("[data-current-user]").forEach(element => {
        element.textContent = user.name;
    });
}

function bindLogoutButton() {
    const logoutButton = document.getElementById("logoutButton");
    if (!logoutButton) {
        return;
    }

    logoutButton.addEventListener("click", async () => {
        try {
            await authRequest("/logout");
        } finally {
            window.location.href = "/login.html";
        }
    });
}

async function initializeAuthentication() {
    bindLogoutButton();

    if (isAuthPage()) {
        try {
            await authRequest("/me");
            window.location.href = "/index.html";
        } catch (error) {
            return null;
        }
        return null;
    }

    const user = await authRequest("/me");
    updateCurrentUser(user);
    return user;
}

function getAuthReadyPromise() {
    return authReadyPromise;
}

document.addEventListener("DOMContentLoaded", () => {
    setupThemeToggle();
    authReadyPromise = initializeAuthentication();
});
