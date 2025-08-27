#!/bin/bash

echo "========================================"
echo "Spring Boot Swagger Example Application"
echo "========================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Error: Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Using Maven Wrapper..."
    MVN_CMD="./mvnw"
    
    # Download Maven wrapper if not exists
    if [ ! -f "$MVN_CMD" ]; then
        echo "Downloading Maven Wrapper..."
        mvn -N io.takari:maven:wrapper
    fi
else
    MVN_CMD="mvn"
    echo "Maven version: $(mvn -version | head -n 1)"
fi

echo ""
echo "Building the application..."
$MVN_CMD clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "Starting the application..."
    echo ""
    echo "The application will be available at:"
    echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "  - API Docs:   http://localhost:8080/v3/api-docs"
    echo ""
    echo "Press Ctrl+C to stop the application."
    echo "========================================"
    echo ""
    
    java -jar target/spring-boot-swagger-example-0.0.1-SNAPSHOT.jar
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi