# Campus Job Board

A full-stack web application that connects university students with on-campus and local employment opportunities. Built with **Spring Boot 4**, **Spring Security**, **Thymeleaf**, and **MySQL**.

## Features

- **Role-based access control** — separate dashboards for Students, Employers, and Admins
- **Job listings** — employers post, update, and manage job openings
- **Job applications** — students apply to jobs; duplicate applications are prevented and handled gracefully
- **User authentication** — BCrypt-hashed passwords, form-based login, secure session management
- **Admin panel** — user and listing management
- **Exception handling** — global handler with user-friendly error pages

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4 |
| Security | Spring Security 6, BCryptPasswordEncoder |
| Persistence | Spring Data JPA, Hibernate, MySQL 8 |
| Frontend | Thymeleaf, Thymeleaf Spring Security extras |
| Validation | Jakarta Bean Validation |
| Build | Maven 3 |

## Project Structure

```
src/main/java/com/example/campusjobboard/
├── model/          # JPA entities: User, Job, JobApplication
├── repository/     # Spring Data JPA interfaces
├── service/        # Business logic interfaces + implementations
├── security/       # SecurityConfig, CustomUserDetailsService
├── exception/      # Custom exceptions + GlobalExceptionHandler
└── enums/          # Role, Status, JobStatus, ApplicationStatus
```

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+

### Database Setup

```sql
CREATE DATABASE campusjobboard_db;
```

### Configuration

Set the following environment variables before running (or copy `src/main/resources/application-local.properties.example`):

```bash
DB_URL=jdbc:mysql://localhost:3306/campusjobboard_db
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

### Run

```bash
cd campusjobboard
./mvnw spring:boot:run
```

The app starts on `http://localhost:8080`.

## Author

Fadeelay — fadlullahlawal2@gmail.com
