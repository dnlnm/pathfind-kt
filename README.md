# Pathfind Android App

Android application for the Pathfind self-hosted bookmark manager. Built with modern Android development practices, this app allows you to seamlessly manage, search, and save your bookmarks on the go.

## Features

- **Bookmark Management**: Create, read, update, and delete bookmarks directly from your device.
- **Collections & Tags**: Organize and categorize your saved links.
- **Native UI/UX**: Polished, platform-specific design leveraging Material 3 and built specifically for Android.
- **Browse & Search**: Powerful native search functionalities and a dedicated Browse tab for discovery.
- **Android Share Intent**: Instantly save links to your Pathfind instance directly from any Android application (browsers, news feeds, etc.) via the standard device share sheet.
- **Self-Hosted Integration**: Easily point the app to your custom Pathfind Server URL and authenticate using your API Token.

## Tech Stack

- [Kotlin](https://kotlinlang.org/) - Primary language
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative Android UI toolkit
- [Material 3](https://m3.material.io/) - Design system and foundational UI components
- [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for backend REST API interactions
- [Coil](https://coil-kt.github.io/coil/) - Image loading backed by Kotlin Coroutines
- [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Asynchronous preference management for authentication and settings

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Latest recommended version)
- Kotlin and Android SDKs configured in your environment
- Java 17+

### Installation & Setup

1. Clone the repository and navigate to the android app directory:
   ```bash
   cd pathfind-kt
   ```
2. Open the configured project in **Android Studio**.
3. Wait for Gradle to sync and download all necessary dependencies automatically.

### Configuration

Upon first launching the application, you'll be prompted to enter your Pathfind instance details:
1. **Server URL**: The URL pointing to your self-hosted Pathfind application (e.g., `https://bookmarks.yourdomain.com`).
2. **API Token**: A secure personal access token generated from your web server's settings panel.

### Development

To build and run the debug application:

- Connect a physical Android device with USB Debugging enabled, or launch an Android Virtual Device (AVD) using the built-in emulator.
- Click the **Run** button (`Shift + 10`) in Android Studio.

Alternatively, you can build from the command line:

```bash
# MacOS / Linux
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

The resulting debug APK will be available under `app/build/outputs/apk/debug/app-debug.apk`.
