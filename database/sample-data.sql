USE student_expense_tracker;

INSERT INTO users (name, email, password) VALUES
    ('Demo Student', 'student@example.com', '$2a$10$slYDycaazW.3.TJXOytsIOrQsNSZ8r2u1nkNPa1P8OfS1eOAYuoXm')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    password = VALUES(password);

INSERT INTO expenses (user_id, amount, category, date, description) VALUES
    ((SELECT id FROM users WHERE email = 'student@example.com'), 120.50, 'FOOD', '2026-03-02', 'College canteen lunch'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 80.00, 'TRAVEL', '2026-03-03', 'Bus pass recharge'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 650.00, 'BOOKS', '2026-03-07', 'Data structures reference book'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 210.75, 'FOOD', '2026-03-10', 'Snacks with friends'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 150.00, 'OTHERS', '2026-03-15', 'Printing assignments'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 95.00, 'TRAVEL', '2026-03-20', 'Auto fare to library'),
    ((SELECT id FROM users WHERE email = 'student@example.com'), 340.00, 'FOOD', '2026-03-24', 'Dinner after lab work');

INSERT INTO budgets (user_id, limit_amount, budget_month) VALUES
    ((SELECT id FROM users WHERE email = 'student@example.com'), 1400.00, '2026-03')
ON DUPLICATE KEY UPDATE
    limit_amount = VALUES(limit_amount);
