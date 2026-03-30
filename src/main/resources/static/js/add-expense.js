document.addEventListener("DOMContentLoaded", async () => {
    try {
        await getAuthReadyPromise();
    } catch (error) {
        return;
    }
    setDefaultDate("date");
    document.getElementById("expenseForm").addEventListener("submit", submitExpenseForm);
});

async function submitExpenseForm(event) {
    event.preventDefault();

    const payload = {
        amount: parseFloat(document.getElementById("amount").value),
        category: document.getElementById("category").value,
        date: document.getElementById("date").value,
        description: document.getElementById("description").value.trim()
    };

    try {
        await apiRequest("/expenses", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        showMessage("formMessage", "Expense added successfully.");
        document.getElementById("expenseForm").reset();
        setDefaultDate("date");
    } catch (error) {
        showMessage("formMessage", error.message, "error");
    }
}
