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
| 🐘 Database    | MS SQL Server |
| 🛡 Security    | JWT Authentication       |
| 🐳 DevOps      | Docker & Docker Compose  |
| 🔁 CI/CD       | GitHub Actions           |

---

## 📂 Project Structure

TODO 

```
algebra-social-network/
├── frontend/ # React app
├── backend/ # Spring Boot app
│ └── src/
├── .github/workflows/ # GitHub Actions CI/CD //TDOD
├── docker-compose.yml //TODO
└── README.md
```

---

## 🔐 Authentication Endpoints

| Method | Endpoint                    | Description          |
|--------|-----------------------------|----------------------|
| POST   | `/api/v1/auth/register`     | Register a new user  |
| POST   | `/api/v1/auth/login`        | Login and get token  |

---

## 👤 User Endpoints

| Method | Endpoint                          | Description        |
|--------|-----------------------------------|--------------------|
| PUT    | `/api/v1/users/{userId}`          | Update user info   |
| DELETE | `/api/v1/users/{userId}`          | Delete user        |
| GET    | `/api/v1/users`                   | List all users     |

---

## 📝 Post Endpoints

| Method | Endpoint                                 | Description             |
|--------|------------------------------------------|-------------------------|
| GET    | `/api/v1/posts`                          | Get all posts           |
| POST   | `/api/v1/posts`                          | Create new post         |
| POST   | `/api/v1/posts/{id}/rate`                | Rate a post             |
| POST   | `/api/v1/posts/{id}/comments`            | Add comment to post     |
| GET    | `/api/v1/posts/{postId}/comments`        | Get comments for post   |
| GET    | `/api/v1/posts/{id}`                     | Get post by ID          |
| GET    | `/api/v1/posts/user/{userId}`            | Get posts by user       |

---

## 🤝 Friends Endpoints

| Method | Endpoint                                 | Description                  |
|--------|------------------------------------------|------------------------------|
| POST   | `/api/v1/friends/request/{userId}`       | Send friend request          |
| POST   | `/api/v1/friends/decline/{requestId}`    | Decline friend request       |
| POST   | `/api/v1/friends/approve/{requestId}`    | Approve friend request       |
| GET    | `/api/v1/friends/requests`               | View friend requests         |
| DELETE | `/api/v1/friends/remove/{userId}`        | Remove a friend              |
