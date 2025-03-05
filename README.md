# Hotel-API

## **⚛ Technology Stack:**

- **Backend**: Java 21, Spring Boot (Web, Data JPA, Validation), Liquibase, Lombok, MapStruct)
- **Database**: H2
- **Dependency Management**: Maven
- **Testing**: JUnit, Mockito, Spring Boot Test (`@DataJpaTest`, `@ActiveProfiles`), AssertJ, Datafaker)
- **Logging**: Log4j2
- **Containerization**: Docker, Docker Compose (For testing purposes only)
- **API documentation**: Swagger

## **✅ Prerequisites**

1. **Docker** and **Docker Compose** installed.
2. Basic knowledge of Docker and RESTful APIs.

## **🚀 Getting Started**

### Clone the Repository
```bash
https://github.com/VladislavKolos/Hotel-API
```
1. Open IntelliJ IDEA.
2. Go to **File** > **New** > **Project from Version Control...**.
3. Paste the repository URL and select a directory.
4. Click **Clone** and **Trust Project**.

## **🔧 Environment Configuration**

The project includes an `.env` file in the root directory, pre-configured for testing purposes.

> **Note:** Avoid storing sensitive data in `.env` files in production.

## **🛠 Running Tests**

To execute tests:
```bash
docker-compose --profile test up -d
mvn test
```
To stop and remove containers:
```bash
docker-compose down
```

## **▶ Running the Application Locally**

```bash
mvn spring-boot:run
```

Swagger UI is available after starting the application:
[http://localhost:8092/property-view/swagger-ui](http://localhost:8092/property-view/swagger-ui)

## **🔎 Troubleshooting**

- **Logs not appearing:** Ensure the `logs` directory has correct permissions.
- **Database connection issues during testing:** Verify `.env` and `docker-compose.yaml` configurations.