# Election Management System

A full-stack web application for managing elections, political parties, candidates, voters, voting, results, and dashboard statistics.

The system uses a React frontend, a Spring Boot REST API backend, and PostgreSQL running through Docker.

---

## Project Overview

The Election Management System provides a secure and structured way to manage election activities.

The system supports:

- Administrator authentication
- Election management
- Political party management
- Candidate management
- Voter registration and management
- Secure vote submission
- Duplicate-vote prevention
- Election results
- Dashboard statistics
- Political party logo upload
- Candidate image upload
- JWT-based protected API routes

---

## Technology Stack

### Backend

- Java 17
- Spring Boot 3.x
- Maven
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- JWT authentication
- Bean Validation
- Lombok
- PostgreSQL Driver

### Frontend

- React 18
- Vite
- React Router 6
- Axios
- React Context API
- CSS / Tailwind CSS

### Database and Tools

- PostgreSQL 16
- Docker Desktop
- Docker Compose
- pgAdmin 4
- Postman
- Git and GitHub

---

## Application Architecture

```text
React Frontend
      |
      | Axios HTTP Requests + JWT
      v
Spring Boot REST Controllers
      |
      v
Service Layer
      |
      v
Repository Layer
      |
      v
PostgreSQL Database
```

The backend follows a standard layered architecture:

- **Controller Layer:** receives and routes HTTP requests
- **Service Layer:** contains business logic and validation rules
- **Repository Layer:** communicates with PostgreSQL through Spring Data JPA
- **Entity Layer:** represents database tables
- **DTO Layer:** controls request and response data
- **Security Layer:** handles authentication and JWT validation

---

## Main Features

### Authentication

- Administrator login
- JWT token generation
- Protected routes
- Current authenticated user endpoint
- Logout through token removal on the frontend

### Election Management

- Create an election
- View all elections
- View one election
- Update an election
- Delete an election
- Change election status

### Political Party Management

- Create political parties
- Upload party logos
- Save uploaded logo filenames in PostgreSQL
- View, update, and delete political parties

### Candidate Management

- Register candidates
- Assign candidates to elections
- Assign candidates to political parties
- Upload candidate photos
- View, update, and delete candidates

### Voter Management

- Register voters
- Validate voter age
- Store voter identification details
- View, update, and delete voters

### Voting

- View open elections
- View election candidates
- Submit votes
- Prevent duplicate voting
- Validate that a candidate belongs to the selected election

### Results and Dashboard

- View election results
- Count votes per candidate
- Display election, party, candidate, voter, and vote totals

---

## Project Structure

Place this README file in the main project folder:

```text
Election-Management-System/
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── uploads/
│   └── application.properties
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── .env

```

---

## Prerequisites

Install the following before running the project:

- Java 17 or newer
- Node.js and npm
- Docker Desktop
- IntelliJ IDEA or another Java IDE
- Visual Studio Code or another React editor
- Git
- Postman

Check the installed versions:

```powershell
java -version
node --version
npm --version
docker --version
docker compose version
```

---

## Database Configuration

The project uses PostgreSQL through Docker.

### Docker Compose Configuration

The PostgreSQL configuration is:

```text
Database: ElectionDB
Username: postgres
Password: postgres
Port: 5432
```

The pgAdmin configuration is:

```text
URL: http://localhost:5050
Email: admin@election.com
Password: admin123
```

### Start PostgreSQL and pgAdmin

Open a terminal in the folder containing `docker-compose.yml` and run:

```powershell
docker compose up -d
```

Check the running containers:

```powershell
docker compose ps
```

Expected status:

```text
election_postgres   Up (healthy)
election_pgadmin    Up
```

### Stop the containers

```powershell
docker compose stop
```

### Start existing containers again

```powershell
docker compose start
```

### Remove containers without deleting the database volume

```powershell
docker compose down
```

> Do not use `docker compose down -v` unless you intentionally want to delete all PostgreSQL data.

---

## pgAdmin Connection

Open:

```text
http://localhost:5050
```

Sign in with:

```text
Email: admin@election.com
Password: admin123
```

Register a server using:

```text
Name: Election Database
Host name/address: postgres
Port: 5432
Maintenance database: ElectionDB
Username: postgres
Password: postgres
```

---

## Backend Setup

Open the `backend` folder in IntelliJ IDEA.

The backend database settings should include:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ElectionDB
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

### Run the backend from IntelliJ

Open:

```text
ElectionManagementApplication.java
```

Run the `main` method.

### Run the backend from the terminal

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

A successful startup includes:

```text
Tomcat started on port 8080
Started ElectionManagementApplication
```

Backend URL:

```text
http://localhost:8080
```

API base URL:

```text
http://localhost:8080/api
```

---

## Default Administrator Account

The application creates a default administrator during development:

```text
Username: admin
Password: admin123
Email: admin@ems.local
Role: ADMIN
```

> Change the default credentials and JWT secret before production deployment.

---

## Frontend Setup

Open the `frontend` folder in Visual Studio Code or another editor.

Install dependencies:

```powershell
cd frontend
npm install
```

Create or update the `.env` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_UPLOAD_BASE_URL=http://localhost:8080/uploads
```

Run the frontend:

```powershell
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

---

## Correct Startup Order

Start the system in this order:

```text
1. Docker Desktop
2. PostgreSQL and pgAdmin containers
3. Spring Boot backend
4. React frontend
```

Commands:

```powershell
docker compose up -d
```

Then run Spring Boot in IntelliJ, and finally:

```powershell
cd frontend
npm run dev
```

---

## API Authentication

Login endpoint:

```http
POST /api/Auth/Login
```

Example request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

The response returns a JWT token.

For protected endpoints, send:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

Current authenticated user:

```http
GET /api/Auth/Me
```

---

## Main API Endpoints

### Authentication

```text
POST /api/Auth/Login
GET  /api/Auth/Me
```

### Elections

```text
GET    /api/Elections
GET    /api/Elections/{id}
POST   /api/Elections
PUT    /api/Elections/{id}
DELETE /api/Elections/{id}
```

### Political Parties

```text
GET    /api/PoliticalParties
GET    /api/PoliticalParties/{id}
POST   /api/PoliticalParties
PUT    /api/PoliticalParties/{id}
DELETE /api/PoliticalParties/{id}
POST   /api/PoliticalParties/upload
```

### Candidates

```text
GET    /api/Candidates
GET    /api/Candidates/{id}
POST   /api/Candidates
PUT    /api/Candidates/{id}
DELETE /api/Candidates/{id}
POST   /api/Candidates/upload
```

### Voters

```text
GET    /api/Voters
GET    /api/Voters/{id}
POST   /api/Voters
PUT    /api/Voters/{id}
DELETE /api/Voters/{id}
```

### Voter Portal

```text
POST /api/VoterPortal/Login
GET  /api/VoterPortal/Elections
GET  /api/VoterPortal/Candidates/{electionId}
POST /api/VoterPortal/Vote
```

### Votes, Results, and Dashboard

```text
GET /api/Votes
GET /api/Results/{electionId}
GET /api/Dashboard
```

---

## Image Upload

Political party logos are uploaded using:

```http
POST /api/PoliticalParties/upload
```

Candidate photos are uploaded using:

```http
POST /api/Candidates/upload
```

Use `multipart/form-data` with:

```text
Key: file
Type: File
```

Supported formats:

```text
.jpg
.jpeg
.png
```

Maximum file size:

```text
5 MB
```

Uploaded files are stored in:

```text
uploads/parties/
uploads/candidates/
```

Only the generated filename is saved in PostgreSQL.

Example party logo URL:

```text
http://localhost:8080/uploads/parties/example-logo.png
```

Example candidate photo URL:

```text
http://localhost:8080/uploads/candidates/example-photo.png
```

---

## Validation and Error Handling

The backend includes:

- Bean Validation annotations
- `@Valid` request validation
- Custom exceptions
- Global exception handling
- Meaningful HTTP status codes
- Validation error messages
- Duplicate data protection
- Duplicate vote protection
- File size and file type validation

Common HTTP status codes:

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
413 Payload Too Large
415 Unsupported Media Type
500 Internal Server Error
```

---

## Testing

The backend APIs were tested using Postman.

Recommended testing order:

```text
1. Login
2. Current authenticated user
3. Election CRUD
4. Political party CRUD
5. Party logo upload
6. Candidate CRUD
7. Candidate image upload
8. Voter CRUD
9. Open election
10. Voter portal login
11. View open elections
12. View candidates
13. Submit vote
14. View votes
15. View results
16. View dashboard
```

---

## Git and GitHub Submission

Initialize Git in the project root:

```powershell
git init
git add .
git commit -m "Complete Election Management System"
```

Create a GitHub repository, then connect and push:

```powershell
git branch -M main
git remote add origin YOUR_GITHUB_REPOSITORY_URL
git push -u origin main
```

Repository URL:

```text
Add your GitHub repository link here.
```

---

## Generative AI Acknowledgement

Generative AI tools were used only for educational support, debugging guidance, understanding framework concepts, exploring implementation approaches, and reviewing documentation.

All group members are responsible for reviewing, understanding, testing, explaining, and defending the submitted project code during the demonstration and viva session.

---

## Security Notes

Before production deployment:

- Change the default administrator password
- Use a secure JWT secret
- Do not commit real credentials to GitHub
- Use environment variables for secrets
- Disable SQL query logging
- Use HTTPS
- Restrict CORS origins
- Validate all uploaded files
- Back up the PostgreSQL database

---

## Troubleshooting

### PostgreSQL connection failed

Confirm Docker is running:

```powershell
docker compose ps
```

Confirm PostgreSQL is healthy and port `5432` is available.

### Backend port is already in use

Change:

```properties
server.port=8081
```

Then update the frontend `.env` file.

### Frontend cannot connect to backend

Confirm:

```text
Backend: http://localhost:8080
Frontend: http://localhost:5173
```

Check:

```properties
app.cors.allowed-origins=http://localhost:5173
```

### Request returns 401 Unauthorized

Log in again and send:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

### JSON request returns unsupported media type

Use:

```text
Body → raw → JSON
Content-Type: application/json
```

### File upload fails

Use:

```text
Body → form-data
Key: file
Type: File
```

Do not manually set the multipart Content-Type header.

---

## Submission Deliverables

The final submission should include:

- Full backend source code
- Full frontend source code
- GitHub repository link
- PostgreSQL schema or ERD
- README file
- Project report
- Individual contribution details
- Generative AI acknowledgement
- Presentation slides

---

## License

This project was developed for educational purposes as a university group final project.
