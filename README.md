# Student Expense Tracker

Student Expense Tracker is a full-stack web application built with Spring Boot, MySQL, HTML, CSS, and vanilla JavaScript. It helps students record expenses, monitor spending patterns, manage a monthly budget, and visualize category-wise trends with charts. The project includes session-based login and registration so each user sees only their own data.

## Folder Structure

```text
student-expense-tracker/
├── database/
│   ├── sample-data.sql
│   └── schema.sql
├── postman/
│   └── student-expense-tracker.postman_collection.json
├── src/
│   └── main/
│       ├── java/com/studentexpensetracker/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── exception/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   └── StudentExpenseTrackerApplication.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── css/styles.css
│               ├── js/
│               ├── add-expense.html
│               ├── edit-expense.html
│               ├── index.html
│               ├── login.html
│               └── register.html
└── pom.xml
```

## Features

- Add, view, edit, and delete expenses
- Categorize expenses into Food, Travel, Books, and Others
- Track total, daily, and monthly spending
- View category-wise and month-wise charts using Chart.js
- Set a monthly budget and show alerts when spending exceeds it
- Search and filter expenses on the dashboard
- Export expenses to CSV
- Toggle between light and dark mode
- Register, login, logout, and session-based access control
- User-specific expenses and budgets
- Validation and centralized exception handling for APIs

## Backend API Endpoints

### Authentication APIs

- `POST /auth/register` - Register a new user and start a session
- `POST /auth/login` - Login and create a session
- `GET /auth/logout` - Logout and invalidate the session
- `GET /auth/me` - Get the currently logged-in user

### Expense APIs

- `POST /api/expenses` - Add expense
- `GET /api/expenses` - Get all expenses
- `GET /api/expenses/{id}` - Get a single expense
- `PUT /api/expenses/{id}` - Update an expense
- `DELETE /api/expenses/{id}` - Delete an expense
- `GET /api/expenses/summary` - Get totals and category summary

### Budget APIs

- `POST /api/budgets` - Set or update a monthly budget
- `GET /api/budgets/check?month=YYYY-MM` - Check budget usage for a month

## MySQL Schema

Use the SQL scripts from [`database/schema.sql`](database/schema.sql) and [`database/sample-data.sql`](database/sample-data.sql).

### Tables

- `users`
  - `id` BIGINT primary key
  - `name` VARCHAR(100)
  - `email` VARCHAR(150) unique
  - `password` VARCHAR(255) storing BCrypt hash
- `expenses`
  - `id` BIGINT primary key
  - `user_id` BIGINT foreign key to `users`
  - `amount` DECIMAL(10,2) with positive amount constraint
  - `category` ENUM with allowed categories
  - `date` DATE
  - `description` VARCHAR(255)
- `budgets`
  - `id` BIGINT primary key
  - `user_id` BIGINT foreign key to `users`
  - `limit_amount` DECIMAL(10,2) with positive amount constraint
  - `budget_month` CHAR(7) unique per user in `YYYY-MM` format

## Step-by-Step Run Instructions

1. Install Java 17+, Maven, and MySQL 8+.
2. Create or verify a MySQL user and password.
3. Update the database credentials in [`src/main/resources/application.properties`](src/main/resources/application.properties).
4. Run the schema script:

```sql
SOURCE /absolute/path/to/student-expense-tracker/database/schema.sql;
```

5. Optionally insert sample records:

```sql
SOURCE /absolute/path/to/student-expense-tracker/database/sample-data.sql;
```

6. Start the Spring Boot app:

```bash
mvn spring-boot:run
```

7. Open the app in your browser:

```text
http://localhost:8080/login.html
```

8. Test APIs with the Postman collection at [`postman/student-expense-tracker.postman_collection.json`](postman/student-expense-tracker.postman_collection.json).

## Authentication Flow

1. Open `http://localhost:8080/login.html`.
2. Register a new account or use the sample user from the SQL seed.
3. After login, the session is stored in `HttpSession`.
4. Protected expense and budget APIs return `401 Unauthorized` if the session is missing.
5. The frontend redirects unauthenticated users back to the login page.

## Sample Request Payloads

### Register

```json
{
  "name": "Asha",
  "email": "asha@example.com",
  "password": "password123"
}
```

### Login

```json
{
  "email": "asha@example.com",
  "password": "password123"
}
```

### Add Expense

```json
{
  "amount": 150.75,
  "category": "FOOD",
  "date": "2026-03-29",
  "description": "Lunch at campus cafeteria"
}
```

### Set Budget

```json
{
  "limitAmount": 2500,
  "month": "2026-03"
}
```

## Notes

- The frontend is served directly from Spring Boot static resources.
- `spring.jpa.hibernate.ddl-auto=update` is enabled for easier local development.
- Sample SQL login: `student@example.com` / `password`
- Swagger UI is available at `http://localhost:8080/swagger-ui/index.html` after starting the application.
