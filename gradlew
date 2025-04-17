name: Build and Release APK

on:
  push:
    branches:
      - master

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      # Check out the repository code
      - name: Checkout code
        uses: actions/checkout@v4

      # Set up JDK for Android
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      # Set up Android SDK
      - name: Setup Android SDK
        uses: android-actions/setup-android@v3

      # Fix line endings for gradlew and make executable
      - name: Fix gradlew line endings
        run: |
          sed -i 's/\r$//' gradlew
          chmod +x gradlew

      # Install specific Android build tools and platforms (adjust versions as needed)
      - name: Install Android Build Tools
        run: |
          sdkmanager "build-tools;33.0.0" "platforms;android-33"

      # Build the release APK
      - name: Build Release APK
        env:
          KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
        run: ./gradlew assembleRelease --stacktrace

      # Upload the APK as an artifact (optional, for debugging)
      - name: Upload APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-release
          path: app/build/outputs/apk/release/app-release.apk

      # Create a GitHub Release and upload the APK
      - name: Create GitHub Release
        uses: softprops/action-gh-release@v2
        with:
          tag_name: v${{ github.run_number }}
          name: Release ${{ github.run_number }}
          body: Automated release for commit ${{ github.sha }}
          files: app/build/outputs/apk/release/app-release.apk
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
