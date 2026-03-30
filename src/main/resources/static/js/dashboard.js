let allExpenses = [];
let categoryChart;
let monthlyChart;

document.addEventListener("DOMContentLoaded", async () => {
    try {
        await getAuthReadyPromise();
    } catch (error) {
        return;
    }
    document.getElementById("budgetMonth").value = new Date().toISOString().slice(0, 7);
    bindDashboardEvents();
    await loadDashboard();
});

function bindDashboardEvents() {
    document.getElementById("searchInput").addEventListener("input", renderFilteredExpenses);
    document.getElementById("categoryFilter").addEventListener("change", renderFilteredExpenses);
    document.getElementById("exportCsvBtn").addEventListener("click", exportToCsv);
    document.getElementById("budgetForm").addEventListener("submit", saveBudget);
    document.getElementById("budgetMonth").addEventListener("change", event => refreshBudgetStatus(event.target.value));
}

async function loadDashboard() {
    try {
        const [expenses, summary] = await Promise.all([
            apiRequest("/expenses"),
            apiRequest("/expenses/summary")
        ]);

        allExpenses = expenses;
        renderExpenses(expenses);
        updateSummaryCards(summary);
        renderCharts(summary);
        await refreshBudgetStatus(document.getElementById("budgetMonth").value);
    } catch (error) {
        renderErrorState(error.message);
    }
}

function renderErrorState(message) {
    document.getElementById("expenseTableBody").innerHTML = `<tr><td colspan="6" class="empty-state">${message}</td></tr>`;
}

function updateSummaryCards(summary) {
    document.getElementById("totalSpending").textContent = formatCurrency(summary.totalSpending);
    document.getElementById("todaySpending").textContent = formatCurrency(summary.todaySpending);
    document.getElementById("monthlySpending").textContent = formatCurrency(summary.currentMonthSpending);
}

function renderExpenses(expenses) {
    const tableBody = document.getElementById("expenseTableBody");

    if (!expenses.length) {
        tableBody.innerHTML = `<tr><td colspan="6" class="empty-state">No expenses found.</td></tr>`;
        return;
    }

    tableBody.innerHTML = expenses.map(expense => `
        <tr>
            <td>${expense.id}</td>
            <td>${expense.date}</td>
            <td>${expense.description}</td>
            <td>${formatCategory(expense.category)}</td>
            <td>${formatCurrency(expense.amount)}</td>
            <td>
                <div class="table-actions">
                    <a class="secondary-btn" href="/edit-expense.html?id=${expense.id}">Edit</a>
                    <button class="danger-btn" type="button" onclick="deleteExpense(${expense.id})">Delete</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderFilteredExpenses() {
    const filtered = getFilteredExpenses();
    renderExpenses(filtered);
}

function getFilteredExpenses() {
    const searchTerm = document.getElementById("searchInput").value.trim().toLowerCase();
    const selectedCategory = document.getElementById("categoryFilter").value;

    return allExpenses.filter(expense => {
        const matchesSearch = expense.description.toLowerCase().includes(searchTerm);
        const matchesCategory = !selectedCategory || expense.category === selectedCategory;
        return matchesSearch && matchesCategory;
    });
}

async function deleteExpense(id) {
    const confirmed = window.confirm("Are you sure you want to delete this expense?");
    if (!confirmed) {
        return;
    }

    try {
        await apiRequest(`/expenses/${id}`, { method: "DELETE" });
        await loadDashboard();
    } catch (error) {
        alert(error.message);
    }
}

function renderCharts(summary) {
    const categoryLabels = Object.keys(summary.categoryTotals || {});
    const categoryValues = Object.values(summary.categoryTotals || {});
    const monthlyLabels = Object.keys(summary.monthlyTotals || {});
    const monthlyValues = Object.values(summary.monthlyTotals || {});

    if (categoryChart) {
        categoryChart.destroy();
    }
    if (monthlyChart) {
        monthlyChart.destroy();
    }

    categoryChart = new Chart(document.getElementById("categoryChart"), {
        type: "pie",
        data: {
            labels: categoryLabels.map(formatCategory),
            datasets: [{
                data: categoryValues,
                backgroundColor: ["#0f766e", "#d97706", "#2563eb", "#ef4444"]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "bottom"
                }
            }
        }
    });

    monthlyChart = new Chart(document.getElementById("monthlyChart"), {
        type: "bar",
        data: {
            labels: monthlyLabels,
            datasets: [{
                label: "Monthly Spending",
                data: monthlyValues,
                backgroundColor: "#0f766e"
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

async function saveBudget(event) {
    event.preventDefault();

    const month = document.getElementById("budgetMonth").value;
    const limitAmount = parseFloat(document.getElementById("limitAmount").value);

    try {
        await apiRequest("/budgets", {
            method: "POST",
            body: JSON.stringify({ month, limitAmount })
        });
        document.getElementById("limitAmount").value = "";
        await refreshBudgetStatus(month);
    } catch (error) {
        document.getElementById("budgetSummary").textContent = error.message;
    }
}

async function refreshBudgetStatus(month) {
    try {
        const status = await apiRequest(`/budgets/check?month=${encodeURIComponent(month)}`);
        const budgetAlert = document.getElementById("budgetAlert");
        const budgetSummary = document.getElementById("budgetSummary");

        budgetAlert.textContent = status.message;
        budgetAlert.className = `budget-badge ${status.exceeded ? "warning" : "success"}`;

        if (status.limitAmount <= 0) {
            budgetAlert.className = "budget-badge muted";
        }

        budgetSummary.innerHTML = `
            <strong>Month:</strong> ${status.month}<br>
            <strong>Budget:</strong> ${formatCurrency(status.limitAmount)}<br>
            <strong>Spent:</strong> ${formatCurrency(status.spentAmount)}<br>
            <strong>Remaining:</strong> ${formatCurrency(status.remainingAmount)}
        `;
    } catch (error) {
        document.getElementById("budgetSummary").textContent = error.message;
    }
}

function exportToCsv() {
    const expensesToExport = getFilteredExpenses();
    const rows = [
        ["ID", "Date", "Description", "Category", "Amount"],
        ...expensesToExport.map(expense => [
            expense.id,
            expense.date,
            `"${expense.description.replaceAll("\"", "\"\"")}"`,
            expense.category,
            expense.amount
        ])
    ];

    const csvContent = rows.map(row => row.join(",")).join("\n");
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "expenses.csv";
    link.click();
    URL.revokeObjectURL(url);
}
