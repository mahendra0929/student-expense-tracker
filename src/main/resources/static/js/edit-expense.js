let expenseId;

document.addEventListener("DOMContentLoaded", async () => {
    try {
        await getAuthReadyPromise();
    } catch (error) {
        return;
    }

    const params = new URLSearchParams(window.location.search);
    expenseId = params.get("id");

    if (!expenseId) {
        showMessage("formMessage", "Expense ID is missing from the URL.", "error");
        return;
    }

    document.getElementById("editExpenseForm").addEventListener("submit", updateExpense);
    await loadExpense();
});

async function loadExpense() {
    try {
        const expense = await apiRequest(`/expenses/${expenseId}`);
        document.getElementById("amount").value = expense.amount;
        document.getElementById("category").value = expense.category;
        document.getElementById("date").value = expense.date;
        document.getElementById("description").value = expense.description;
    } catch (error) {
        showMessage("formMessage", error.message, "error");
    }
}

async function updateExpense(event) {
    event.preventDefault();

    const payload = {
        amount: parseFloat(document.getElementById("amount").value),
        category: document.getElementById("category").value,
        date: document.getElementById("date").value,
        description: document.getElementById("description").value.trim()
    };

    try {
        await apiRequest(`/expenses/${expenseId}`, {
            method: "PUT",
            body: JSON.stringify(payload)
        });
        showMessage("formMessage", "Expense updated successfully.");
        setTimeout(() => {
            window.location.href = "/index.html";
        }, 1000);
    } catch (error) {
        showMessage("formMessage", error.message, "error");
    }
}
