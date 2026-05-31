# 🎟️ BookMyEvent — Event Management Backend

Spring Boot REST API backend for BookMyEvent, a full-stack event management platform where users can create, browse, and book events with role-based access control.

🔗 **Live Demo:** [bookmyevent25.vercel.app](https://bookmyevent25.vercel.app)  
🔗 **Frontend Repo:** [bookmyevent-frontend](https://github.com/DubeySumit25/bookmyevent-frontend)

---

## 🚀 Features

- JWT-based user authentication & authorization
- Role-based access control (USER / ADMIN / ORGANIZER)
- Create, update, and delete events
- Book events and manage bookings
- REST APIs for all operations
- Dockerized for easy deployment

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot, Java |
| Auth | JWT (JSON Web Tokens) |
| Database | MySQL |
| Deployment | Docker, Render |

---

## 🔐 API Endpoints

### Auth
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/auth/register` | Public |
| POST | `/auth/login` | Public |

### Events
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/events` | Public |
| GET | `/events/{id}` | Public |
| POST | `/events` | ADMIN |
| PUT | `/events/{id}` | ADMIN |
| DELETE | `/events/{id}` | ADMIN |

### Bookings
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/bookings` | USER |
| GET | `/bookings/my` | USER |
| DELETE | `/bookings/{id}` | USER |

---

## ⚙️ Setup Locally

```bash
# Clone the repo
git clone https://github.com/DubeySumit25/bookmyevent-backend.git
cd bookmyevent-backend

# Configure application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookmyevent
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update

# Run
./mvnw spring-boot:run
```

---

## 🐳 Docker

```bash
docker build -t bookmyevent-backend .
docker run -p 8080:8080 bookmyevent-backend
```

---

## 👨‍💻 Author

**Sumit Dubey**  
[LinkedIn](https://www.linkedin.com/in/sumit-dubey-9a0226322/) • [GitHub](https://github.com/DubeySumit25) • [LeetCode](https://leetcode.com/u/anonymousvenom/)
