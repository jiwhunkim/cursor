# Spring Boot Swagger Example

This is a sample Spring Boot 3.3.5 application demonstrating the integration of Swagger/OpenAPI 3.0 documentation using springdoc-openapi.

## Features

- Spring Boot 3.3.5
- RESTful API with CRUD operations
- Swagger/OpenAPI 3.0 documentation
- Request validation
- Global exception handling
- Paginated responses
- Health check endpoints
- Lombok for reduced boilerplate code

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
spring-boot-swagger-example/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── config/
│   │   │       │   └── OpenApiConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── HealthController.java
│   │   │       │   └── UserController.java
│   │   │       ├── model/
│   │   │       │   ├── ApiResponse.java
│   │   │       │   ├── CreateUserRequest.java
│   │   │       │   ├── PagedResponse.java
│   │   │       │   ├── UpdateUserRequest.java
│   │   │       │   └── User.java
│   │   │       └── SpringBootSwaggerExampleApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

## Getting Started

### 1. Clone the repository

```bash
cd /workspace/spring-boot-swagger-example
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/spring-boot-swagger-example-0.0.1-SNAPSHOT.jar
```

### 4. Access the application

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## API Endpoints

### User Management APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users (paginated) |
| GET | `/api/v1/users/{id}` | Get user by ID |
| POST | `/api/v1/users` | Create a new user |
| PUT | `/api/v1/users/{id}` | Update an existing user |
| DELETE | `/api/v1/users/{id}` | Delete a user |
| GET | `/api/v1/users/search` | Search users by query |

### Health Check APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Basic health check |
| GET | `/api/health/detailed` | Detailed health information |

## Configuration

### Swagger Configuration

The Swagger/OpenAPI configuration can be customized in `application.properties` or `application.yml`:

```properties
# Swagger UI path
springdoc.swagger-ui.path=/swagger-ui.html

# API docs path
springdoc.api-docs.path=/v3/api-docs

# Enable/disable Swagger UI
springdoc.swagger-ui.enabled=true
```

### Security Configuration

The application includes a JWT bearer token security scheme configuration. In a production environment, you would implement actual authentication/authorization logic.

## Using Swagger UI

1. Navigate to http://localhost:8080/swagger-ui.html
2. You'll see all available endpoints grouped by tags
3. Click on any endpoint to expand it
4. Click "Try it out" to test the endpoint
5. Fill in the required parameters
6. Click "Execute" to send the request
7. View the response below

### Features in Swagger UI

- **Try it out**: Test endpoints directly from the browser
- **Request/Response examples**: View sample requests and responses
- **Model schemas**: Explore data models used in the API
- **Authentication**: Configure bearer token for secured endpoints

## Development

### Adding New Endpoints

1. Create a new controller class or add methods to existing controllers
2. Use appropriate Swagger annotations:
   - `@Tag` - Group related endpoints
   - `@Operation` - Describe the operation
   - `@ApiResponses` - Document possible responses
   - `@Parameter` - Document parameters

Example:
```java
@GetMapping("/example")
@Operation(summary = "Example endpoint", description = "This is an example")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "404", description = "Not found")
})
public ResponseEntity<String> example() {
    return ResponseEntity.ok("Example response");
}
```

### Customizing OpenAPI Documentation

Edit `OpenApiConfig.java` to customize:
- API title, version, and description
- Contact information
- License details
- Server URLs
- Security schemes

## Dependencies

Main dependencies used in this project:

- `spring-boot-starter-web` - Spring Web MVC
- `spring-boot-starter-validation` - Bean validation
- `springdoc-openapi-starter-webmvc-ui` - OpenAPI 3.0 documentation
- `lombok` - Reduce boilerplate code
- `spring-boot-devtools` - Development tools

## Troubleshooting

### Common Issues

1. **Port already in use**: Change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

2. **Swagger UI not loading**: Ensure springdoc dependencies are correctly added and the application is running

3. **Validation not working**: Make sure `spring-boot-starter-validation` is included in dependencies

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Contact

For questions or support, please contact:
- Email: support@example.com
- Website: http://www.example.com/support