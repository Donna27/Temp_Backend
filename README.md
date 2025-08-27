# Temp Backend

A Spring Boot application with a simple "Hello World" REST API.

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## How to run

1. Navigate to the project directory:
   ```
   cd Temp_Backend
   ```

2. Run the application using Maven:
   ```
   mvn spring-boot:run
   ```

3. The application will start on port 8080.

## API Endpoints

### GET /get
Returns a JSON response with a "Hello World" message.

**Response:**
```json
{
  "message": "Hello World"
}
```

**Example:**
```
curl http://localhost:8080/get
```

## Project Structure

- `src/main/java/com/kbtg/tempbackend/` - Java source files
- `src/main/resources/` - Configuration files
- `pom.xml` - Maven configuration
