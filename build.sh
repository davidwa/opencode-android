#!/bin/bash
set -e

echo "========================================="
echo "OpenCode Android - Build Script"
echo "========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Check dependencies
check_dependency() {
    if ! command -v "$1" &> /dev/null; then
        echo -e "${RED}✗ $1 is not installed${NC}"
        return 1
    fi
    echo -e "${GREEN}✓ $1 found${NC}"
    return 0
}

echo "Checking dependencies..."
check_dependency java
check_dependency gradle || check_dependency ./gradlew

echo ""
echo "Building OpenCode Android APK..."
echo ""

# Download gradle wrapper if needed
if [ ! -f "gradlew" ]; then
    echo "Setting up Gradle wrapper..."
    gradle wrapper --gradle-version 8.2
fi

# Make gradlew executable
chmod +x gradlew

# Build release APK
echo "Building release APK..."
./gradlew assembleRelease --console=plain

# Check if build succeeded
if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
    echo ""
    echo -e "${GREEN}✓ Build successful!${NC}"
    echo ""
    echo "APK location:"
    echo "  app/build/outputs/apk/release/app-release-unsigned.apk"
    echo ""
    echo "To install on Android:"
    echo "  adb install app/build/outputs/apk/release/app-release-unsigned.apk"
    echo ""
    
    # Get file size
    ls -lh app/build/outputs/apk/release/app-release-unsigned.apk
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi
