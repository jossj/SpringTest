# SpringTest

Spring Boot 3.4.2 web application with Java 23, Maven, Spring Security, and Lombok.

## Quick Start

```bash
cd SpringTest

# Run the application
mvn spring-boot:run

# Or build and run
mvn clean package
java -jar target/spring-test-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Endpoint | Auth Required | Description |
|----------|---------------|-------------|
| GET `/api/public/hello` | No | Public endpoint |
| GET `/api/secure` | Yes | Requires authentication |
| GET `/api/users` | Yes | List all users |
| GET `/api/users/{id}` | Yes | Get user by ID |
| POST `/api/users` | Yes | Create new user |
| PUT `/api/users/{id}` | Yes | Update user |
| DELETE `/api/users/{id}` | Yes | Delete user |

## Default Credentials

- **Username:** `admin`
- **Password:** `admin123`

⚠️ Change these in `application.properties` for production!

## Tech Stack

- Java 23
- Spring Boot 3.4.2
- Spring Web
- Spring Security
- Lombok
- Maven

## Testing

```bash
mvn test
```
