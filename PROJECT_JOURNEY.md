# Realtime Messaging Platform - Project Journey

## Tech Stack

Backend:
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- BCrypt
- JWT (upcoming)
- WebSocket (upcoming)
- Redis (upcoming)

Frontend:
- React.js
- Vite
- React Router
- Axios (upcoming)

---

# DAY 1 - Backend Setup

## Completed
✔ Spring Boot project setup  
✔ MySQL database connection  
✔ Maven dependencies setup  
✔ Backend package structure  

Architecture:
Controller → Service → Repository → MySQL

## Learned
- Entity = Database table representation
- Repository = Database operations
- Service = Business logic
- Controller = Handles HTTP requests

Git Commit:
DAY-01 Spring Boot setup

---

# DAY 2 - Frontend Setup

## Completed
✔ React project using Vite  
✔ React Router setup  
✔ Initial folder structure  

Routes:
- / → Login
- /register → Register
- /chat → Chat page

## Learned
React → Axios → Spring Boot API → Database

Git Commit:
DAY-02 React setup with Vite and Router

---

# DAY 3 - Authentication Theory

## Concepts

Authentication:
Verify who the user is.

Authorization:
Check what the user can access.

Registration Flow:

User Password
      ↓
BCrypt
      ↓
Hash stored in Database


Login Flow:

Email + Password
      ↓
Check password using BCrypt
      ↓
Generate JWT
      ↓
Return token to frontend


JWT Request:

React
  ↓
Authorization: Bearer Token
  ↓
Spring Security
  ↓
Controller

---

# DAY 4 - User Registration API

## Completed

✔ User Entity
✔ UserRepository
✔ RegisterRequest DTO
✔ Validation
✔ BCrypt PasswordEncoder
✔ UserService register logic
✔ AuthController
✔ SecurityFilterChain

API:

POST /api/auth/register


Registration Flow:

Request DTO
      ↓
Controller
      ↓
Service
      ↓
Check email exists
      ↓
BCrypt password
      ↓
Repository
      ↓
MySQL


Problems Solved:
- Lombok not working in STS
- Removed Lombok and wrote constructors/getters/setters manually
- Spring Security blocked API (401)
- Added SecurityFilterChain and allowed /api/auth/**


Interview Points:

Why BCrypt?
→ Passwords are stored as one-way hashes for security.

Why DTO?
→ Separates API data from database entities.

Why SecurityFilterChain?
→ Defines public and protected APIs.


Git Commit:
DAY-04 User registration with BCrypt

---

# Current Progress

Backend:
[✔] Setup
[✔] Registration
[ ] Login
[ ] JWT
[ ] WebSocket
[ ] Redis
[ ] Docker
[ ] AWS

Frontend:
[✔] Setup
[ ] Authentication UI
[ ] Chat UI

---

Next Day:
DAY 5 → Login API + JWT Authentication