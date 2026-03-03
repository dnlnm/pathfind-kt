# PathFind Android
 
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=for-the-badge&logo=jetpack-compose)](https://developer.android.com/jetpack/compose)
[![Android](https://img.shields.io/badge/Android-Native-3DDC84?style=for-the-badge&logo=android)](https://www.android.com/)

A native Android companion app for [PathFind](https://github.com/dnlnm/pathfind). Built with modern Android development practices, this app allows you to seamlessly manage, search, and save your bookmarks on the go.

![PathFind Android Mockup](pathfind_android_mockup_1772558582648.png)

---

## 🌐 The PathFind Ecosystem

- **[PathFind Web](https://github.com/dnlnm/pathfind)**: The core self-hosted server and dashboard.
- **[PathFind Extension](https://github.com/dnlnm/pathfind-ext)**: Browser extension for Chrome, Edge, and Firefox.
- **[PathFind iOS](https://github.com/dnlnm/pathfind-ios)**: Native SwiftUI mobile app for iPhone.
- **[PathFind Android](https://github.com/dnlnm/pathfind-kt)**: Native Kotlin & Compose mobile app.

---

## ✨ Features

- **📱 Native Material 3 UI**: Polished, platform-specific design leveraging Material You dynamics.
- **📥 Android Share Intent**: Instantly save links to your PathFind instance directly from any Android application via the standard share sheet.
- **🔍 Global Search**: Powerful native search functionalities to find your bookmarks instantly.
- **📁 Collections & Tags**: Full access to organize and categorize your saved links.
- **🔒 Secure Authentication**: Easily connect to your self-hosted server using a personal API Token.

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Latest Version)
- Java 17+
- A running [PathFind](https://github.com/dnlnm/pathfind) instance.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/dnlnm/pathfind-kt.git
   cd pathfind-kt
   ```
2. Open the project in **Android Studio**.
3. Wait for Gradle to sync.

### Connectivity

Upon first launch, you will need:
1. **Server URL**: Your PathFind instance URL (e.g., `https://pathfind.yourdomain.com`).
2. **API Token**: Generated from your PathFind Web settings.

---

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with **Material 3**
- **Networking**: [Retrofit](https://square.github.io/retrofit/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Data Persistence**: [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

---

## 📄 License

MIT © [dnlnm](https://github.com/dnlnm)

