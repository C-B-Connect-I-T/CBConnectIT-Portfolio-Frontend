#!/bin/zsh

# Script to fix KSP cache corruption issues
# This script cleans KSP caches and related build artifacts

echo "🧹 Cleaning KSP caches and build artifacts..."

# Stop any running Gradle daemons
echo "Stopping Gradle daemons..."
./gradlew --stop

# Clean the project
echo "Running Gradle clean..."
./gradlew clean

# Remove KSP caches from all modules
echo "Removing KSP caches..."
rm -rf */build/kspCaches/
rm -rf site/build/kspCaches/
rm -rf landing/build/kspCaches/
rm -rf core/build/kspCaches/
rm -rf data/build/kspCaches/
rm -rf backoffice/build/kspCaches/

# Remove Gradle cache directories
echo "Removing Gradle caches..."
rm -rf .gradle/
rm -rf build/
rm -rf */build/

# Remove Kotlin JS store
echo "Removing Kotlin JS store..."
rm -rf kotlin-js-store/

# Remove node_modules if present
if [ -d "node_modules" ]; then
    echo "Removing node_modules..."
    rm -rf node_modules/
fi

# Remove package-lock files
find . -name "package-lock.json" -type f -delete

echo "✅ Cache cleanup complete!"
echo ""
echo "Now rebuilding the project..."
./gradlew build

echo ""
echo "✨ Done! Your project should be ready to use."
