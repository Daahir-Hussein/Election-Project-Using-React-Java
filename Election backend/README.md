# Spring Boot Backend

This folder is the converted Java backend for the Election Management System.

## Requirements

- Java 17+
- PostgreSQL 14+

## Database

Create:

```sql
CREATE DATABASE election_management;
```

Default connection:

```text
jdbc:postgresql://localhost:5432/election_management
username: postgres
password: postgres
```

Override it with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

## Run on Windows

```powershell
.\mvnw.cmd spring-boot:run
```

## Test

```powershell
.\mvnw.cmd test
```

## Default login

```text
admin / admin123
```

Change `ADMIN_PASSWORD` and `JWT_SECRET` before deployment.

The API runs at `http://localhost:8080`.
