#!/bin/bash

# MeBill Android App Build Script
# This script helps build and manage the MeBill Android application

echo "🔨 MeBill Android App Build Script"
echo "=================================="

# Check if Gradle is available
if ! command -v ./gradlew &> /dev/null; then
    echo "❌ Gradle wrapper not found. Make sure you're in the project root directory."
    exit 1
fi

# Function to display help
show_help() {
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  build      Build debug APK"
    echo "  release    Build release APK"
    echo "  install    Build and install debug APK to connected device"
    echo "  clean      Clean build files"
    echo "  test       Run unit tests"
    echo "  lint       Run lint analysis"
    echo "  help       Show this help message"
    echo ""
}

# Parse command line arguments
case "$1" in
    "build" | "")
        echo "🔨 Building debug APK..."
        ./gradlew assembleDebug
        if [ $? -eq 0 ]; then
            echo "✅ Debug APK built successfully!"
            echo "📁 Location: app/build/outputs/apk/debug/app-debug.apk"
        else
            echo "❌ Build failed!"
            exit 1
        fi
        ;;
    
    "release")
        echo "🔨 Building release APK..."
        ./gradlew assembleRelease
        if [ $? -eq 0 ]; then
            echo "✅ Release APK built successfully!"
            echo "📁 Location: app/build/outputs/apk/release/app-release.apk"
        else
            echo "❌ Release build failed!"
            exit 1
        fi
        ;;
    
    "install")
        echo "📱 Building and installing debug APK..."
        ./gradlew installDebug
        if [ $? -eq 0 ]; then
            echo "✅ App installed successfully!"
        else
            echo "❌ Installation failed! Make sure a device is connected and USB debugging is enabled."
            exit 1
        fi
        ;;
    
    "clean")
        echo "🧹 Cleaning build files..."
        ./gradlew clean
        if [ $? -eq 0 ]; then
            echo "✅ Clean completed successfully!"
        else
            echo "❌ Clean failed!"
            exit 1
        fi
        ;;
    
    "test")
        echo "🧪 Running unit tests..."
        ./gradlew test
        if [ $? -eq 0 ]; then
            echo "✅ All tests passed!"
        else
            echo "❌ Some tests failed!"
            exit 1
        fi
        ;;
    
    "lint")
        echo "🔍 Running lint analysis..."
        ./gradlew lint
        if [ $? -eq 0 ]; then
            echo "✅ Lint analysis completed!"
            echo "📁 Report location: app/build/reports/lint-results.html"
        else
            echo "❌ Lint analysis failed!"
            exit 1
        fi
        ;;
    
    "help")
        show_help
        ;;
    
    *)
        echo "❌ Unknown option: $1"
        echo ""
        show_help
        exit 1
        ;;
esac

echo ""
echo "🎉 Task completed!"