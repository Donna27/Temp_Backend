# C4 Architecture Diagrams

This document contains C4 model diagrams for the Clean Architecture Spring Boot application.

## C4 Model Overview

The C4 model consists of 4 levels:
- **Level 1**: System Context - Shows how the system fits into the world
- **Level 2**: Container - Shows the high-level technical building blocks  
- **Level 3**: Component - Shows components within a container
- **Level 4**: Code - Shows how components are implemented (class diagrams)

---

## Level 1: System Context Diagram

Shows the big picture - how users interact with the system and external dependencies.

```mermaid
C4Context
    title System Context Diagram - User Management System

    Person(user, "User", "A person who wants to register and authenticate")
    Person(admin, "Administrator", "A person who manages the system")
    
    System(userSystem, "User Management System", "Allows users to register, login, and manage profiles using Clean Architecture")
    
    System_Ext(database, "SQLite Database", "Stores user information securely")
    System_Ext(email, "Email Service", "Sends notification emails (future)")
    
    Rel(user, userSystem, "Registers, logs in, views profile", "HTTPS/REST")
    Rel(admin, userSystem, "Manages users", "HTTPS/REST") 
    Rel(userSystem, database, "Reads from and writes to", "JDBC")
    Rel(userSystem, email, "Sends emails using", "SMTP (future)")
    
    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```

---

## Level 2: Container Diagram  

Shows the high-level technical building blocks and how they interact.

```mermaid
C4Container
    title Container Diagram - User Management System

    Person(user, "User", "A person using the system")
    
    Container_Boundary(c1, "User Management System") {
        Container(web, "Web Application", "Spring Boot", "Delivers REST API endpoints for user management")
        Container(security, "Security Layer", "Spring Security + JWT", "Handles authentication and authorization")
    }
    
    ContainerDb(database, "Database", "SQLite", "Stores user accounts, credentials, and profile information")
    
    Rel(user, web, "Makes API calls to", "JSON/HTTPS")
    Rel(web, security, "Validates requests using", "JWT")
    Rel(web, database, "Reads from and writes to", "JDBC/JPA")
    Rel(security, database, "Validates credentials against", "JDBC/JPA")
    
    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```

---

## Level 3: Component Diagram

Shows the internal components following Clean Architecture principles.

```mermaid
C4Component
    title Component Diagram - Clean Architecture Layers

    Container_Boundary(web, "Spring Boot Web Application") {
        Component(userController, "UserControllerClean", "Spring Controller", "Handles HTTP requests for user operations")
        Component(userAppService, "UserApplicationService", "Application Service", "Orchestrates user operations")
        Component(registerUseCase, "RegisterUserUseCase", "Use Case", "Handles user registration business logic")
        Component(loginUseCase, "LoginUserUseCase", "Use Case", "Handles user login business logic")
        Component(profileUseCase, "GetUserProfileUseCase", "Use Case", "Handles user profile retrieval")
        Component(userEntity, "UserEntity", "Domain Entity", "Pure business logic for User")
        Component(userRepoPort, "UserRepositoryPort", "Repository Interface", "Contract for user data access")
        Component(userRepoAdapter, "UserRepositoryAdapter", "JPA Repository", "Implements user data access")
        Component(userJpaEntity, "UserJpaEntity", "JPA Entity", "Database mapping for User")
    }
    
    ContainerDb(database, "SQLite Database", "Database", "Stores user data")
    
    Rel(userController, userAppService, "Uses")
    Rel(userAppService, registerUseCase, "Delegates to")
    Rel(userAppService, loginUseCase, "Delegates to")
    Rel(userAppService, profileUseCase, "Delegates to")
    Rel(registerUseCase, userRepoPort, "Uses")
    Rel(loginUseCase, userRepoPort, "Uses")
    Rel(profileUseCase, userRepoPort, "Uses")
    Rel(userRepoAdapter, userRepoPort, "Implements")
    Rel(userRepoAdapter, userJpaEntity, "Uses")
    Rel(userJpaEntity, database, "Persists to")
    
    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="1")
```

---

## Level 3a: Interface Layer Components

```mermaid
C4Component
    title Interface Layer - Controllers and DTOs

    Container_Boundary(interface, "Interface Layer") {
        Component(userController, "UserControllerClean", "Spring Controller", "REST endpoints for user operations")
        Component(helloController, "HelloController", "Spring Controller", "Hello world endpoint")
        Component(loginReq, "LoginRequest", "DTO", "Login request data")
        Component(loginRes, "LoginResponse", "DTO", "Login response with JWT")
        Component(regReq, "UserRegistrationRequest", "DTO", "User registration data")
        Component(regRes, "UserRegistrationResponse", "DTO", "Registration response")
        Component(profileRes, "UserProfileResponse", "DTO", "User profile data")
    }
    
    Component_Ext(appService, "UserApplicationService", "Application Service", "Business logic orchestrator")
    
    Rel(userController, appService, "Uses")
    Rel(userController, loginReq, "Receives")
    Rel(userController, loginRes, "Returns")
    Rel(userController, regReq, "Receives")
    Rel(userController, regRes, "Returns")
    Rel(userController, profileRes, "Returns")
```

---

## Level 3b: Application Layer Components

```mermaid
C4Component
    title Application Layer - Use Cases and Services

    Container_Boundary(application, "Application Layer") {
        Component(userAppService, "UserApplicationService", "Application Service", "Orchestrates user operations")
        Component(registerUseCase, "RegisterUserUseCase", "Use Case", "User registration logic")
        Component(loginUseCase, "LoginUserUseCase", "Use Case", "User authentication logic")
        Component(profileUseCase, "GetUserProfileUseCase", "Use Case", "Profile retrieval logic")
    }
    
    Component_Ext(userRepoPort, "UserRepositoryPort", "Domain Interface", "Data access contract")
    Component_Ext(passwordPort, "PasswordEncoderPort", "Domain Interface", "Password encryption contract")
    Component_Ext(jwtPort, "JwtServicePort", "Domain Interface", "JWT operations contract")
    
    Rel(userAppService, registerUseCase, "Delegates to")
    Rel(userAppService, loginUseCase, "Delegates to")
    Rel(userAppService, profileUseCase, "Delegates to")
    Rel(registerUseCase, userRepoPort, "Uses")
    Rel(registerUseCase, passwordPort, "Uses")
    Rel(loginUseCase, userRepoPort, "Uses")
    Rel(loginUseCase, passwordPort, "Uses")
    Rel(loginUseCase, jwtPort, "Uses")
    Rel(profileUseCase, userRepoPort, "Uses")
```

---

## Level 3c: Infrastructure Layer Components

```mermaid
C4Component
    title Infrastructure Layer - Adapters and External Services

    Container_Boundary(infrastructure, "Infrastructure Layer") {
        Component(userRepoAdapter, "UserRepositoryAdapter", "JPA Adapter", "Database access implementation")
        Component(passwordAdapter, "PasswordEncoderAdapter", "BCrypt Adapter", "Password encryption implementation")
        Component(jwtAdapter, "JwtServiceAdapter", "JWT Adapter", "JWT operations implementation")
        Component(userJpaEntity, "UserJpaEntity", "JPA Entity", "Database mapping")
        Component(jwtFilter, "JwtAuthenticationFilterClean", "Security Filter", "Request authentication")
    }
    
    ContainerDb(database, "SQLite Database", "Database", "User data storage")
    Component_Ext(jwtUtil, "JwtUtil", "Utility", "JWT token operations")
    Component_Ext(bcrypt, "BCrypt", "Library", "Password hashing")
    
    Rel(userRepoAdapter, userJpaEntity, "Uses")
    Rel(userJpaEntity, database, "Persists to")
    Rel(passwordAdapter, bcrypt, "Uses")
    Rel(jwtAdapter, jwtUtil, "Uses")
    Rel(jwtFilter, jwtAdapter, "Validates with")
```

Shows the key classes and their relationships in the Domain layer.

```mermaid
classDiagram
    class UserEntity {
        -Long id
        -String email
        -String password
        -String firstname
        -String lastname
        -String phoneNumber
        -LocalDate birthday
        +UserEntity(Builder)
        +validateEmail() boolean
        +validatePhoneNumber() boolean
        +isValidBirthday() boolean
        +getFullName() String
        +Builder: Builder
    }
    
    class Builder {
        -Long id
        -String email
        -String password
        -String firstname
        -String lastname
        -String phoneNumber
        -LocalDate dateOfBirth
        +withId(Long) Builder
        +withEmail(String) Builder
        +withPassword(String) Builder
        +withFirstname(String) Builder
        +withLastname(String) Builder
        +withPhoneNumber(String) Builder
        +withDateOfBirth(LocalDate) Builder
        +build() UserEntity
    }
    
    class UserRepositoryPort {
        <<interface>>
        +findByEmail(String) Optional~UserEntity~
        +save(UserEntity) UserEntity
        +existsByEmail(String) boolean
        +findById(Long) Optional~UserEntity~
    }
    
    class JwtServicePort {
        <<interface>>
        +generateToken(String) String
        +validateToken(String) boolean
        +extractEmail(String) String
    }
    
    class PasswordEncoderPort {
        <<interface>>
        +encode(String) String
        +matches(String, String) boolean
    }
    
    UserEntity *-- Builder : contains
    UserEntity ..> UserRepositoryPort : used by
    UserEntity ..> JwtServicePort : used with
    UserEntity ..> PasswordEncoderPort : used with
```

---

## Level 4: Code Diagram - Application Layer

Shows the Use Cases and Application Service structure.

```mermaid
classDiagram
    class UserApplicationService {
        -RegisterUserUseCase registerUserUseCase
        -LoginUserUseCase loginUserUseCase
        -GetUserProfileUseCase getUserProfileUseCase
        +registerUser(UserRegistrationRequest) UserRegistrationResponse
        +loginUser(LoginRequest) LoginResponse
        +getUserProfile(String) Optional~UserProfileResponse~
    }
    
    class RegisterUserUseCase {
        -UserRepositoryPort userRepository
        -PasswordEncoderPort passwordEncoder
        +execute(UserRegistrationRequest) UserRegistrationResponse
        -validateRegistrationRequest(UserRegistrationRequest) void
        -createUserEntity(UserRegistrationRequest) UserEntity
    }
    
    class LoginUserUseCase {
        -UserRepositoryPort userRepository
        -PasswordEncoderPort passwordEncoder
        -JwtServicePort jwtService
        +execute(LoginRequest) LoginResponse
        -validateCredentials(String, String, UserEntity) boolean
    }
    
    class GetUserProfileUseCase {
        -UserRepositoryPort userRepository
        +execute(String) Optional~UserProfileResponse~
        -convertToResponse(UserEntity) UserProfileResponse
    }
    
    UserApplicationService --> RegisterUserUseCase : uses
    UserApplicationService --> LoginUserUseCase : uses
    UserApplicationService --> GetUserProfileUseCase : uses
    
    RegisterUserUseCase ..> UserRepositoryPort : depends on
    RegisterUserUseCase ..> PasswordEncoderPort : depends on
    
    LoginUserUseCase ..> UserRepositoryPort : depends on
    LoginUserUseCase ..> PasswordEncoderPort : depends on
    LoginUserUseCase ..> JwtServicePort : depends on
    
    GetUserProfileUseCase ..> UserRepositoryPort : depends on
```

---

## Level 4: Deployment Diagram

Shows how the system is deployed in different environments.

```mermaid
C4Deployment
    title Deployment Diagram - User Management System

    Deployment_Node(dev, "Development Environment", "Local Machine") {
        Deployment_Node(jvm, "JVM", "Java 17") {
            Container(app, "Spring Boot Application", "JAR", "User Management API")
        }
        Deployment_Node(db, "Database", "SQLite") {
            ContainerDb(sqlite, "SQLite Database", "File", "users.db")
        }
    }
    
    Deployment_Node(prod, "Production Environment", "Cloud/Server") {
        Deployment_Node(appServer, "Application Server", "Tomcat/Embedded") {
            Container(prodApp, "Spring Boot Application", "JAR", "User Management API")
        }
        Deployment_Node(dbServer, "Database Server", "PostgreSQL/MySQL") {
            ContainerDb(prodDb, "Production Database", "RDBMS", "User data storage")
        }
    }
    
    Rel(app, sqlite, "Reads/Writes", "JDBC")
    Rel(prodApp, prodDb, "Reads/Writes", "JDBC/Connection Pool")
```

---

## Clean Architecture Layer Dependencies

```mermaid
graph TD
    A[Interface Layer<br/>Controllers & DTOs] --> B[Application Layer<br/>Use Cases & Services]
    B --> C[Domain Layer<br/>Entities & Ports]
    D[Infrastructure Layer<br/>Adapters & JPA] --> C
    D --> E[(External Systems<br/>Database, JWT, BCrypt)]
    
    style A fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    style B fill:#f3e5f5,stroke:#4a148c,stroke-width:2px  
    style C fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px
    style D fill:#fff3e0,stroke:#e65100,stroke-width:2px
    style E fill:#ffebee,stroke:#b71c1c,stroke-width:2px
    
    classDef interfaceLayer fill:#e1f5fe,stroke:#01579b
    classDef applicationLayer fill:#f3e5f5,stroke:#4a148c
    classDef domainLayer fill:#e8f5e8,stroke:#1b5e20
    classDef infrastructureLayer fill:#fff3e0,stroke:#e65100
    classDef externalLayer fill:#ffebee,stroke:#b71c1c
```

---

## Key Architectural Patterns

### Dependency Inversion
- High-level modules (Use Cases) don't depend on low-level modules (Infrastructure)
- Both depend on abstractions (Ports/Interfaces)
- Infrastructure layer implements the interfaces defined in Domain layer

### Separation of Concerns
- **Interface Layer**: HTTP concerns, data transformation
- **Application Layer**: Use case orchestration, business workflows  
- **Domain Layer**: Business rules, entities, contracts
- **Infrastructure Layer**: External services, databases, frameworks

### Benefits
- **Testability**: Each layer can be tested in isolation
- **Flexibility**: Easy to change infrastructure without affecting business logic
- **Maintainability**: Clear boundaries and responsibilities
- **Independence**: Domain logic is framework-agnostic

---

*These C4 diagrams provide a comprehensive view of the system architecture following Clean Architecture principles.*
