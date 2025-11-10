# Lees-Waiting: Restaurant Waiting Management System

This is a restaurant waiting management system built with Spring Boot. It allows customers to register for a waiting list and provides administrators with a dashboard to manage the waiting list and table occupancy.

## Features

### For Customers
- Register for the waiting list with the number of people and phone number.
- Receive an estimated waiting time.

### For Administrators
- Secure login/logout.
- Configure the number of 2-person and 4-person tables.
- View a real-time waiting list.
- Manage waiting customers (e.g., mark as 'completed' or 'canceled').
- Manage table status, including customer entry times.
- Clear all data for a fresh start.

## Technologies Used

- **Backend:**
  - Java 21
  - Spring Boot 3
  - Spring Web
  - Spring Data JPA
  - Lombok
- **Frontend:**
  - Thymeleaf
  - HTML, CSS, JavaScript
  - Bootstrap
- **Database:**
  - H2 (for development)
  - PostgreSQL (for production)
- **Build Tool:**
  - Gradle

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Han-WonGeun/lees-waiting.git
    cd lees-waiting
    ```

2.  **Run the application using Gradle:**
    ```bash
    ./gradlew bootRun
    ```

3.  **Access the application:**
    - **Customer View:** [http://localhost:8080/](http://localhost:8080/)
    - **Admin View:** [http://localhost:8080/admin/login](http://localhost:8080/admin/login)
      - Default admin credentials: `admin` / `1234`

## Database Configuration

- The application uses an in-memory H2 database by default for development.
- For production, it's configured to use PostgreSQL. You will need to set the following environment variables:
  - `JDBC_DATABASE_URL`
  - `JDBC_DATABASE_USERNAME`
  - `JDBC_DATABASE_PASSWORD`
