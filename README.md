# Bookstore API

A comprehensive Spring Boot backend for managing a library or bookstore, featuring user authentication, book inventory management, a borrowing system, and AI-powered search integration.

## Technologies

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** (PostgreSQL)
- **Spring Security** (JWT Authentication with Nimbus JOSE)
- **SpringDoc OpenAPI** (Swagger UI)
- **Lombok** & **MapStruct**
- **Gemini AI Integration** (via OpenRouter)
- **Email Service** (Resend API & SMTP)
- **Docker** & **Docker Compose**

## Key Features

### User & Security
- **Registration & Verification**: Users can register and must verify their account via an OTP sent to their email.
- **JWT Authentication**: Secure login and stateless session management.
- **Password Management**: Forgot password and reset password functionality using OTP.
- **Role-Based Access Control**: Different permissions for `USER`, `STAFF`, and `ADMIN` roles.

### Book Management
- **Full CRUD**: Manage books with details like title, author, description, and quantity.
- **Search & Pagination**: Efficient book discovery with paginated results and keyword search.
- **Soft Delete**: Deleting a book moves it to a "trash" state rather than immediate permanent removal.

### Borrowing System
- **Borrow & Return**: Track borrowed books and handle returns.
- **Borrowing History**: Users can view their own history; admins can view all transactions.
- **Status Tracking**: Track statuses like `BORROWING`, `RETURNED`, `OVERDUE`, and `LOST`.

### AI Integration
- **Smart Search**: Integrates with Gemini AI to provide intelligent book recommendations and search capabilities based on user queries.

## Configuration

The application requires several environment variables or configuration in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://your-db-url
spring.datasource.username=your-username
spring.datasource.password=your-password

# JWT
app.jwt.secret=YourSuperSecretKeyWithAtLeast32Characters
app.jwt.expiration-ms=3600000

# AI & External APIs
openrouter.api.key=your-openrouter-key
resend.api.key=your-resend-key

# Mail Server
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Getting Started

### Local Development
1. Clone the repository.
2. Update `application.properties` with your local PostgreSQL and API configurations.
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Docker
The project includes a `Dockerfile` and `docker-compose.yml` for containerized deployment.
```bash
docker-compose up --build
```

## API Documentation

Once the application is running, you can access the Swagger UI for full API documentation at:
`http://localhost:10000/swagger-ui/index.html` (or your configured port)

## Deployment
The backend is configured to work with a frontend hosted at: `https://front-end-library.onrender.com`

---
Developed by [Ky]
