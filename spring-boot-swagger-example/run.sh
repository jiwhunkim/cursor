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

# Detect build tool preference (Gradle takes precedence if both exist)
BUILD_TOOL=""
JAR_PATH=""

if [ -f "build.gradle.kts" ] || [ -f "build.gradle" ]; then
    BUILD_TOOL="gradle"
    JAR_PATH="build/libs/spring-boot-swagger-example.jar"
elif [ -f "pom.xml" ]; then
    BUILD_TOOL="maven"
    JAR_PATH="target/spring-boot-swagger-example-0.0.1-SNAPSHOT.jar"
else
    echo "Error: No build configuration found (pom.xml or build.gradle.kts)"
    exit 1
fi

echo "Detected build tool: $BUILD_TOOL"
echo ""

# Build and run based on detected tool
if [ "$BUILD_TOOL" = "gradle" ]; then
    # Check if Gradle wrapper exists
    if [ -f "./gradlew" ]; then
        GRADLE_CMD="./gradlew"
        echo "Using Gradle Wrapper"
    elif command -v gradle &> /dev/null; then
        GRADLE_CMD="gradle"
        echo "Gradle version: $(gradle -version | grep "Gradle" | head -n 1)"
    else
        echo "Error: Gradle is not installed and wrapper not found."
        exit 1
    fi
    
    echo ""
    echo "Building the application with Gradle..."
    $GRADLE_CMD clean build -x test
    
    BUILD_RESULT=$?
    
elif [ "$BUILD_TOOL" = "maven" ]; then
    # Check if Maven is installed
    if [ -f "./mvnw" ]; then
        MVN_CMD="./mvnw"
        echo "Using Maven Wrapper"
    elif command -v mvn &> /dev/null; then
        MVN_CMD="mvn"
        echo "Maven version: $(mvn -version | head -n 1)"
    else
        echo "Error: Maven is not installed and wrapper not found."
        exit 1
    fi
    
    echo ""
    echo "Building the application with Maven..."
    $MVN_CMD clean package -DskipTests
    
    BUILD_RESULT=$?
fi

if [ $BUILD_RESULT -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo ""
    echo "Starting the application..."
    echo ""
    echo "The application will be available at:"
    echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "  - API Docs:   http://localhost:8080/v3/api-docs"
    echo "  - Health:     http://localhost:8080/api/health"
    echo ""
    echo "Press Ctrl+C to stop the application."
    echo "========================================"
    echo ""
    
    java -jar $JAR_PATH
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi