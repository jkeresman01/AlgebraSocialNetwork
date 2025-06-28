# 📚 algebra-social-network

> Social network platform for Algebra students 

---

## 👨‍💻 PRA Tim 2

**Team Members:**
- Mato Jelen Kralj
- Matija Javor
- Josip Keresman
- Bruno Koren

---

## ⚙️ Tech Stack

| Layer         | Tech                     |
|---------------|--------------------------|
| 🖼 Frontend    | React                    |
| 🚀 Backend     | Spring Boot (Java)       |
| 🐘 Database    | MS SQL Server            |
| 🛡 Security    | JWT Authentication       |
| 🛡 File Storage| AWS S3                   |
| 🐳 DevOps      | Docker & Docker Compose  |
| 🔁 CI/CD       | GitHub Actions           |

---

## 📂 Project Structure

### 📦 Backend Structure

```bash
backend/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/hr/algebra/socialnetwork/
│   │   │   ├── AlgebraSocialNetworkApplication.java
│   │   │   ├── config/               # Configuration classes (CORS, Swagger, AWS, etc.)
│   │   │   ├── controller/           # REST Controllers
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── exception/            # Exception handling
│   │   │   ├── mapper/               # DTO mappers
│   │   │   ├── model/                # JPA Entities
│   │   │   ├── payload/              # Request payloads
│   │   │   ├── repository/           # Spring Data Repositories
│   │   │   ├── s3/                   # AWS S3 Service
│   │   │   ├── security/             # Security configuration
│   │   │   ├── service/              # Business logic services
│   │   │   └── validation/           # Custom annotations & validators

```

### 📦 Frontend Structure

```bash
frontend/src/
├── App.jsx
├── assets/                     # Static assets (images, svg, etc.)
│   └── alg_wd_blur.svg
├── components/
│   ├── common/                 # Reusable components (input, theme, toaster)
│   ├── layout/                 # Layout components (navbar, sidebar)
│   ├── posts/                  # Post-related UI
│   └── profile/                # Profile editing components
├── context/
│   └── AuthContext.jsx         # Authentication context
├── index.css
├── main.jsx
├── pages/                      # Route-level pages
│   ├── EditProfilePage.jsx
│   ├── FriendRequestsPage.jsx
│   ├── FriendsPage.jsx
│   ├── HomePage.jsx
│   ├── LoginPage.jsx
│   ├── ProfilePage.jsx
│   ├── RegisterPage.jsx
│   └── StudentsPage.jsx
├── routes/
│   └── PrivateRoute.jsx        # Protected route wrapper
├── services/                   # API calls and service layer
│   ├── authService.js
│   ├── friendsService.js
│   ├── postsService.js
│   └── usersService.js
├── styles/
│   └── App.css
└── utils/
    └── utils.js                # Helper functions|
```
