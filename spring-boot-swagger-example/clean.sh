#!/bin/bash

echo "========================================"
echo "Cleaning Spring Boot Swagger Example"
echo "========================================"
echo ""

# Clean Maven artifacts
if [ -f "pom.xml" ]; then
    echo "Cleaning Maven artifacts..."
    if [ -d "target" ]; then
        rm -rf target
        echo "  ✓ Removed target directory"
    fi
fi

# Clean Gradle artifacts
if [ -f "build.gradle.kts" ] || [ -f "build.gradle" ]; then
    echo "Cleaning Gradle artifacts..."
    if [ -d "build" ]; then
        rm -rf build
        echo "  ✓ Removed build directory"
    fi
    if [ -d ".gradle" ]; then
        rm -rf .gradle
        echo "  ✓ Removed .gradle directory"
    fi
fi

# Clean IDE files
echo ""
echo "Cleaning IDE files..."
if [ -d ".idea" ]; then
    rm -rf .idea
    echo "  ✓ Removed .idea directory"
fi
if [ -f "*.iml" ]; then
    rm -f *.iml
    echo "  ✓ Removed IntelliJ module files"
fi
if [ -d ".vscode" ]; then
    rm -rf .vscode
    echo "  ✓ Removed .vscode directory"
fi
if [ -d ".settings" ]; then
    rm -rf .settings
    echo "  ✓ Removed Eclipse settings"
fi
if [ -f ".classpath" ]; then
    rm -f .classpath
    echo "  ✓ Removed Eclipse classpath"
fi
if [ -f ".project" ]; then
    rm -f .project
    echo "  ✓ Removed Eclipse project file"
fi

# Clean logs
echo ""
echo "Cleaning logs..."
if [ -d "logs" ]; then
    rm -rf logs
    echo "  ✓ Removed logs directory"
fi
if [ -f "*.log" ]; then
    rm -f *.log
    echo "  ✓ Removed log files"
fi

echo ""
echo "✅ Clean complete!"
echo "========================================"