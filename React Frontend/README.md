# Election Management System — React Frontend

React 18 frontend connected to the Spring Boot election-management API.

## Stack

- React 18
- React Router 6
- Axios
- React Hook Form
- Tailwind CSS
- Vite
- JWT authentication stored in `localStorage`

## Requirements

- Node.js 20 or later
- Spring Boot backend running at `http://localhost:8080`

## Run

```powershell
npm install
npm run dev
```

Open `http://localhost:5173`.

Default administrator login:

```text
Username: admin
Password: admin123
```

## Environment

The included `.env` uses:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_UPLOAD_BASE_URL=http://localhost:8080/uploads
```

## Backend integration

- Login: `POST /api/Auth/Login`
- Session validation: `GET /api/Auth/Me`
- JWT is automatically attached as `Authorization: Bearer <token>`
- Elections, parties, candidates, voters, votes, results and dashboard are loaded through Axios services
- Party and candidate images are uploaded with `multipart/form-data`
- Uploaded images are read from the Spring Boot `/uploads` resource path
