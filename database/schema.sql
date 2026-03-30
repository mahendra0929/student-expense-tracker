CREATE DATABASE IF NOT EXISTS student_expense_tracker;
USE student_expense_tracker;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT chk_users_name CHECK (CHAR_LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_users_email CHECK (CHAR_LENGTH(TRIM(email)) > 0)
);

CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    category ENUM('FOOD', 'TRAVEL', 'BOOKS', 'OTHERS') NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT chk_expenses_description CHECK (CHAR_LENGTH(TRIM(description)) > 0),
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS budgets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    limit_amount DECIMAL(10, 2) NOT NULL CHECK (limit_amount > 0),
    budget_month CHAR(7) NOT NULL,
    CONSTRAINT chk_budget_month_format CHECK (budget_month REGEXP '^[0-9]{4}-(0[1-9]|1[0-2])$'),
    CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_budget_user_month UNIQUE (user_id, budget_month)
);

CREATE INDEX idx_expenses_user_date ON expenses (user_id, date);
