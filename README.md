# Topiks

Topiks is an Android note-taking app that organizes notes into topics and categories, using a messaging-like interface. It supports text notes, file attachments, fuzzy search, PDF export, customizable themes, and local data storage with a focus on privacy.

## License

Topiks is licensed under the GNU General Public License v3.0 (GPL-3.0).

Third-party library (Coil) are licensed under Apache 2.0. See the LICENSE file for details.

Copyright © 2025 GrandSphere Studios
See the LICENSE file for details.

## Features

- **Topic Organization**: Group notes into topics with customizable background colors.
- **File Attachments**: Add images or other files to notes, stored locally.
- **Fuzzy Search**: Search notes within a topic or globally with fuzzy matching.
- **PDF Export**: Export individual or multiple notes to PDF.
- **Customizable Themes**: Select light, dark, or system-default themes.
- **Database Import/Export**: Back up and restore data via database files.
- **Privacy Focused**: Requires no internet or other permissions.

## Technical Information

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room
- **Minimum SDK**: Android 12 (API 31)
- **Target SDK**: Android 14 (API 34)
- **License**: GNU GPL-3.0

## Getting Started

### Prerequisites

- Android Studio (e.g., Koala or later)
- JDK 11
- Kotlin 2.0.20
- Gradle 8.5.2
- Device/Emulator running Android 12 (API 31) or higher

### Build

1. Clone the repository:

   ```bash
   git clone https://github.com/GrandSphere/Topiks
   cd Topiks
   ```

2. Open the project in Android Studio and sync Gradle to download dependencies.

3. Configure signing for release builds:

    - Create a `keystore.jks` file with android studio.

4. Build and run:

    - Select a device or emulator (API 31+).
    - Run via Android Studio

### Dependencies

Key dependencies (see `app/build.gradle`):

- Jetpack Compose (UI)
- Room (database)
- Dagger Hilt v2.51 (dependency injection, partially implemented)
- Jetpack Navigation Compose (navigation)
- Coil and Picasso (image loading)


## Contributing

Contributions are welcome. To contribute:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

Follow Kotlin Coding Conventions and ensure compliance with the GPL-3.0 license.

## Known Issues

Report issues at https://github.com/GrandSphere/Topiks/issues.


## Contact

For questions or feedback, contact:

- vaneeden.a@proton.me
- esterhuyse.a@proton.me
