# Temp Backend - Spring Boot REST API

A comprehensive Spring Boot REST API project with JWT authentication, user management, SQLite database integration, and complete unit test coverage.

## 🚀 Features

- **User Registration** - Register new users with email validation
- **JWT Authentication** - Secure login with JWT token generation
- **User Profile Management** - Retrieve user profile information
- **SQLite Database** - Lightweight database integration with JPA/Hibernate
- **Password Encryption** - BCrypt password hashing for security
- **API Documentation** - Swagger UI integration
- **Comprehensive Testing** - Unit tests, integration tests, and mocking
- **Security Configuration** - Spring Security with custom JWT filter

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **SQLite Database**
- **JWT (JSON Web Tokens)**
- **BCrypt Password Encoder**
- **Swagger/OpenAPI 3**
- **JUnit 5 & Mockito** (Testing)
- **Maven** (Build Tool)

## 📋 API Endpoints

### Public Endpoints
- `GET /get` - Hello World endpoint
- `POST /api/register` - User registration
- `POST /api/login` - User authentication

### Protected Endpoints (Requires JWT Token)
- `GET /api/me` - Get current user profile

### Documentation
- `GET /swagger-ui/index.html` - Swagger UI documentation
- `GET /v3/api-docs` - OpenAPI specification

## 🗄 Database Schema

### User Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    birthday DATE
);
```

## 📝 API Usage Examples

### 1. User Registration
```bash
POST /api/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "firstname": "John",
    "lastname": "Doe",
    "phoneNumber": "1234567890",
    "birthday": "1990-01-01"
}
```

**Response:**
```json
{
    "status": "success",
    "message": "User registered successfully",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "phoneNumber": "1234567890",
    "birthday": "1990-01-01"
}
```

### 2. User Login
```bash
POST /api/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

**Response:**
```json
{
    "status": "success",
    "message": "Login successful",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "user@example.com"
}
```

### 3. Get User Profile
```bash
GET /api/me
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
    "status": "success",
    "message": "User profile retrieved successfully",
    "id": 1,
    "email": "user@example.com",
    "firstname": "John",
    "lastname": "Doe",
    "phoneNumber": "1234567890",
    "birthday": "1990-01-01"
}
```

## 🏗 Project Structure

```
src/
├── main/
│   ├── java/com/kbtg/tempbackend/
│   │   ├── config/
│   │   │   ├── OpenApiConfig.java          # Swagger configuration
│   │   │   ├── PasswordConfig.java         # Password encoder bean
│   │   │   └── SecurityConfig.java         # Security configuration
│   │   ├── controller/
│   │   │   ├── HelloController.java        # Hello World endpoint
│   │   │   └── UserController.java         # User management endpoints
│   │   ├── dto/
│   │   │   ├── LoginRequest.java           # Login request DTO
│   │   │   ├── LoginResponse.java          # Login response DTO
│   │   │   ├── UserProfileResponse.java    # User profile response DTO
│   │   │   └── UserRegistrationResponse.java # Registration response DTO
│   │   ├── filter/
│   │   │   └── JwtAuthenticationFilter.java # JWT authentication filter
│   │   ├── model/
│   │   │   └── User.java                   # User entity
│   │   ├── repository/
│   │   │   └── UserRepository.java         # User data access layer
│   │   ├── service/
│   │   │   └── UserService.java            # User business logic
│   │   ├── util/
│   │   │   └── JwtUtil.java                # JWT utility methods
│   │   └── TempBackendApplication.java     # Main application class
│   └── resources/
│       └── application.properties          # Application configuration
└── test/
    ├── java/com/kbtg/tempbackend/
    │   ├── controller/
    │   │   ├── HelloControllerTest.java     # Controller unit tests
    │   │   └── UserControllerIntegrationTest.java # API integration tests
    │   ├── repository/
    │   │   └── UserRepositoryTest.java      # Repository tests
    │   ├── service/
    │   │   ├── UserServiceTest.java         # Service unit tests
    │   │   └── UserServiceIntegrationTest.java # Service integration tests
    │   ├── util/
    │   │   └── JwtUtilTest.java             # JWT utility tests
    │   └── TempBackendApplicationTests.java # Application context tests
    └── resources/
        └── application-test.properties      # Test configuration
```

## 🔧 Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository:**
```bash
git clone https://github.com/Donna27/Temp_Backend.git
cd Temp_Backend
```

2. **Build the project:**
```bash
mvn clean compile
```

3. **Run the application:**
```bash
mvn spring-boot:run
```

4. **Access the application:**
- API Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# Run only unit tests
mvn test -Dtest="*Test"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"
```

## 🧪 Testing

The project includes comprehensive test coverage:

### Unit Tests
- **UserServiceTest** - Service layer logic with mocking
- **JwtUtilTest** - JWT token operations
- **HelloControllerTest** - Controller endpoints

### Integration Tests
- **UserServiceIntegrationTest** - Service layer with database
- **UserRepositoryTest** - Database operations with @DataJpaTest
- **UserControllerIntegrationTest** - End-to-end API testing

### Test Configuration
- In-memory SQLite database for testing
- Mockito for mocking dependencies
- Spring Boot Test for integration testing
- Separate test properties configuration

## 🔐 Security Features

- **JWT Authentication** - Stateless authentication mechanism
- **Password Encryption** - BCrypt hashing algorithm
- **Protected Routes** - JWT token validation for sensitive endpoints
- **CORS Configuration** - Cross-origin request handling
- **Security Headers** - Standard security headers configuration

## 📚 Configuration

### Database Configuration
```properties
# SQLite Database
spring.datasource.url=jdbc:sqlite:database.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
- **Secret Key** - Configurable JWT signing key
- **Token Validity** - 5 hours default expiration
- **Algorithm** - HMAC SHA-256

### Swagger Configuration
- **API Title** - Temp Backend API
- **Version** - 1.0.0
- **Contact** - KBTG Support

## 🚀 Deployment

### Building for Production
```bash
# Create executable JAR
mvn clean package

# Run the JAR file
java -jar target/temp-backend-0.0.1-SNAPSHOT.jar
```

### Docker Support (Future Enhancement)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/temp-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- **Email**: support@kbtg.com
- **Repository**: [https://github.com/Donna27/Temp_Backend](https://github.com/Donna27/Temp_Backend)

## 🔄 Version History

- **v1.0.0** - Initial release with complete authentication system and testing suite
  - JWT authentication implementation
  - SQLite database integration
  - Comprehensive unit and integration tests
  - Swagger API documentation
  - Security configuration with Spring Security

---

**Made with ❤️ by KBTG Development Team**
