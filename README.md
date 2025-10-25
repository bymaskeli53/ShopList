# 🛒 ShopList

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-24%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-Native-000000?style=for-the-badge&logo=apple&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A modern, accessible shopping list app built with Kotlin Multiplatform**

[Features](#-features) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [Building](#-building) • [Tech Stack](#️-tech-stack)

</div>

---

## 🌟 Overview

ShopList is a **Kotlin Multiplatform** shopping list application that demonstrates modern Android development practices with **MVVM architecture**, **Material Design 3**, and **accessibility-first** design principles. Built with Compose Multiplatform, it runs natively on Android and iOS with shared business logic.

### Why ShopList?

- 🎨 **Beautiful UI**: High-contrast design with Material You dynamic colors (Android 12+)
- ♿ **Accessibility First**: Large touch targets (48dp+), screen reader support, and TTS features
- 🔄 **Reactive Architecture**: Built with Kotlin Flow and StateFlow for seamless data updates
- 🎯 **Clean Code**: MVVM architecture with clear separation of concerns
- 📱 **Multiplatform**: Shared codebase for Android and iOS
- 🗃️ **Offline First**: Local SQLDelight database with instant data persistence
- 🌙 **Dark Mode**: High-contrast dark theme optimized for readability

---

## ✨ Features

### Core Functionality

| Feature | Description | Status |
|---------|-------------|--------|
| 📝 **List Management** | Create, edit, and organize multiple shopping lists | ✅ |
| 🔍 **Smart Search** | Real-time search across list titles and items | ✅ |
| 🗑️ **Swipe to Delete** | Intuitive swipe gesture with undo functionality | ✅ |
| ✅ **Completion Tracking** | Mark lists as completed with visual feedback | ✅ |
| 🔊 **Text-to-Speech** | Voice readback of shopping lists (Turkish language) | ✅ |
| 🎯 **Item Quantity** | Track quantities and amounts for each item | ✅ |
| ✏️ **Inline Editing** | Edit lists and items with smooth animations | ✅ |

### Technical Highlights

- 🎭 **Smooth Animations**: Fade in/out and slide animations throughout the app
- 🌈 **Dynamic Colors**: Adapts to wallpaper colors on Android 12+ (Material You)
- 💾 **Instant Persistence**: All changes saved immediately to local database
- 🔄 **Reactive UI**: Automatic updates when data changes
- 🏗️ **Clean Architecture**: Proper layering with Domain, Data, and Presentation layers
- 🧪 **Testable**: MVVM pattern makes unit testing straightforward

---

## 🏛️ Architecture

ShopList follows **Clean Architecture** principles with **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ ListScreen   │  │ AddScreen    │  │ DetailScreen │      │
│  │ + ViewModel  │  │ + ViewModel  │  │ + ViewModel  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           ↓ ↑
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌────────────────────┐  ┌─────────────────────────┐        │
│  │ ShoppingRepository │  │ Domain Models           │        │
│  │    (Interface)     │  │ - ShoppingList          │        │
│  └────────────────────┘  │ - ShoppingItem          │        │
│                          │ - ShoppingItemInput     │        │
│                          └─────────────────────────┘        │
└─────────────────────────────────────────────────────────────┘
                           ↓ ↑
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────────────────┐  ┌─────────────────────┐      │
│  │ ShoppingRepositoryImpl   │  │ Data Entities       │      │
│  │                          │  │ + Mappers           │      │
│  │ (SQLDelight Database)    │  │                     │      │
│  └──────────────────────────┘  └─────────────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility | Key Components |
|-------|----------------|----------------|
| **Presentation** | UI & User Interaction | Compose screens, ViewModels, Theme |
| **Domain** | Business Logic | Repository interfaces, Domain Models |
| **Data** | Data Management | Repository implementation, Database, Mappers |

> 📖 For detailed architecture documentation, see [MVVM_ARCHITECTURE.md](MVVM_ARCHITECTURE.md).

---

## 🎨 Design Philosophy

### High Contrast & Accessibility

ShopList is designed with **accessibility-first** principles:

- ✅ **WCAG AA compliant** color contrast ratios
- ✅ **48dp minimum** touch targets for all interactive elements
- ✅ **Large, readable fonts** with proper typography hierarchy
- ✅ **Screen reader support** with meaningful content descriptions
- ✅ **Text-to-Speech** for hands-free list navigation
- ✅ **Smooth animations** with proper timing for cognitive accessibility
- ✅ **Search functionality** for quick list discovery

### Color Palette

<div align="center">

| Color | Hex | Usage |
|-------|-----|-------|
| 🖤 **Background** | `#000000` | Pure Black - Main background |
| 🌑 **Surface** | `#1A1A1A` | Dark Gray - Cards and surfaces |
| 💚 **Accent** | `#00E676` | Bright Green - Primary actions |
| ⚪ **Text Primary** | `#FFFFFF` | White - Main text |
| 🔘 **Text Secondary** | `#B0B0B0` | Light Gray - Secondary text |
| 🔴 **Delete** | `#FF5252` | Red - Destructive actions |

</div>

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug | 2024.2.1 or newer
- **JDK** 17 or newer
- **Kotlin** 2.2.0
- **Gradle** 8.11.1
- **Xcode** 15+ (for iOS development)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/shoplist.git
   cd shoplist
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   ```bash
   ./gradlew build
   ```

4. **Run the app**
   - **Android**: Click "Run" or press `Shift + F10`
   - **iOS**: Open `iosApp` in Xcode and run

---

## 🔨 Building

### Android

```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Release build
./gradlew :composeApp:assembleRelease

# Install on connected device
./gradlew :composeApp:installDebug

# Generate signed APK
./gradlew :composeApp:assembleRelease
```

### iOS

```bash
# Open in Xcode
open iosApp/iosApp.xcodeproj

# Or build from command line
xcodebuild -project iosApp/iosApp.xcodeproj \
           -scheme iosApp \
           -configuration Debug
```

### Desktop (JVM)

```bash
# Run desktop application
./gradlew :composeApp:run

# Create distribution
./gradlew :composeApp:createDistributable
```

---

## 🛠️ Tech Stack

### Core Technologies

<div align="center">

| Technology | Purpose | Version |
|:----------:|:-------:|:-------:|
| ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white) | Programming Language | 2.2.0 |
| ![Compose](https://img.shields.io/badge/Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white) | UI Framework | 1.8.2 |
| ![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=flat&logo=kotlin&logoColor=white) | Async Programming | 1.10.2 |
| ![Flow](https://img.shields.io/badge/Flow-7F52FF?style=flat&logo=kotlin&logoColor=white) | Reactive Streams | - |

</div>

### Android Libraries

| Library | Purpose | Version |
|---------|---------|---------|
| **Material3** | UI Components & Theming | Latest |
| **Navigation Compose** | Type-safe Navigation | 2.9.0-rc01 |
| **Lifecycle ViewModel** | State Management | 2.9.1 |
| **SQLDelight** | Database ORM | 2.0.2 |
| **Kotlinx DateTime** | Date/Time handling | 0.6.1 |

### Platform Features

- 🎤 **Android TTS API**: Native Text-to-Speech on Android
- 🍎 **AVSpeechSynthesizer**: Native Text-to-Speech on iOS
- 🎨 **Material You**: Dynamic theming on Android 12+
- 📱 **Compose Multiplatform**: Shared UI across platforms

---

## 📂 Project Structure

```
ShopList/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/com/gundogar/shoplist/
│       │   ├── domain/                    # 🎯 Business logic layer
│       │   │   ├── model/                 # Domain models
│       │   │   │   ├── ShoppingList.kt
│       │   │   │   ├── ShoppingItem.kt
│       │   │   │   └── ShoppingItemInput.kt
│       │   │   └── repository/            # Repository interfaces
│       │   │       └── ShoppingRepository.kt
│       │   │
│       │   ├── data/                      # 💾 Data layer
│       │   │   ├── local/                 # Database drivers
│       │   │   │   └── DatabaseDriverFactory.kt
│       │   │   ├── model/                 # Data entities & mappers
│       │   │   │   └── ShoppingListEntity.kt
│       │   │   └── repository/            # Repository implementations
│       │   │       └── ShoppingRepositoryImpl.kt
│       │   │
│       │   ├── presentation/              # 🎨 UI layer
│       │   │   ├── list/                  # Main list screen
│       │   │   │   ├── ShoppingListScreen.kt
│       │   │   │   ├── ShoppingListViewModel.kt
│       │   │   │   └── components/
│       │   │   │       └── ListRow.kt
│       │   │   ├── add/                   # Add list screen
│       │   │   │   ├── AddItemScreen.kt
│       │   │   │   └── AddItemViewModel.kt
│       │   │   ├── detail/                # Edit list screen
│       │   │   │   ├── DetailScreen.kt
│       │   │   │   └── DetailViewModel.kt
│       │   │   └── theme/                 # App theming
│       │   │       ├── Color.kt
│       │   │       └── Theme.kt
│       │   │
│       │   ├── util/                      # 🔧 Utilities
│       │   │   └── tts/                   # Text-to-Speech
│       │   │       └── TextToSpeechManager.kt
│       │   │
│       │   └── App.kt                     # Root composable & navigation
│       │
│       ├── androidMain/                   # 🤖 Android-specific code
│       │   └── kotlin/com/gundogar/shoplist/
│       │       ├── MainActivity.kt
│       │       ├── data/local/
│       │       │   └── DatabaseDriverFactory.android.kt
│       │       ├── util/tts/
│       │       │   └── TextToSpeechManager.android.kt
│       │       └── presentation/theme/
│       │           └── Theme.android.kt
│       │
│       └── iosMain/                       # 🍎 iOS-specific code
│           └── kotlin/com/gundogar/shoplist/
│               ├── data/local/
│               │   └── DatabaseDriverFactory.ios.kt
│               ├── util/tts/
│               │   └── TextToSpeechManager.ios.kt
│               └── presentation/theme/
│                   └── Theme.ios.kt
│
├── iosApp/                                # iOS application entry point
├── docs/                                  # Documentation & assets
├── CLAUDE.md                              # Development guide
├── MVVM_ARCHITECTURE.md                   # Architecture documentation
└── README.md                              # This file
```

---

## 🧪 Testing

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run Android instrumentation tests
./gradlew connectedAndroidTest

# Run tests with coverage
./gradlew testDebugUnitTestCoverage

# Run all checks (tests + lint)
./gradlew check
```

### Testing Strategy

<div align="center">

| Test Type | Coverage | Tools |
|-----------|----------|-------|
| **Unit Tests** | ViewModels, Mappers, Use Cases | JUnit, Kotlin Test |
| **Integration Tests** | Repository, Database operations | SQLDelight Testing |
| **UI Tests** | Compose screens, User interactions | Compose Test API |

</div>

---

## 🎯 Roadmap

### 🚧 Upcoming Features

- [ ] 🌐 **Cloud Sync** - Firebase/Supabase integration for multi-device sync
- [ ] 👥 **Shared Lists** - Collaborate with family members in real-time
- [ ] 📊 **Analytics** - Shopping statistics and spending insights
- [ ] 🏷️ **Categories** - Organize items by categories (Dairy, Produce, etc.)
- [ ] 📸 **Barcode Scanner** - Quick product addition via barcode
- [ ] 💰 **Price Tracking** - Monitor prices and set budget limits
- [ ] 🔔 **Smart Reminders** - Location-based and time-based notifications
- [ ] 🎨 **Theme Customization** - Custom color schemes and light mode
- [ ] 🌍 **Localization** - Support for multiple languages
- [ ] 📤 **Import/Export** - Backup lists to CSV/JSON

### 🎉 Recently Added

- ✅ **Search Functionality** - Real-time search across all lists and items
- ✅ **Text-to-Speech** - Voice readback in Turkish
- ✅ **Swipe to Delete** - Intuitive gesture with undo
- ✅ **MVVM Architecture** - Clean separation of concerns
- ✅ **Material You** - Dynamic color theming on Android 12+

---

## 🤝 Contributing

Contributions are **welcome**! Whether it's bug fixes, new features, or documentation improvements.

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Make** your changes following the code style
4. **Test** your changes thoroughly
5. **Commit** with descriptive messages
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
6. **Push** to your fork
   ```bash
   git push origin feature/AmazingFeature
   ```
7. **Open** a Pull Request with a clear description

### Code Style Guidelines

- ✅ Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- ✅ Use meaningful variable and function names
- ✅ Write KDoc comments for public APIs
- ✅ Keep functions small and focused (< 50 lines)
- ✅ Follow SOLID principles
- ✅ Add unit tests for new features
- ✅ Update documentation for API changes

### Areas for Contribution

- 🐛 **Bug Fixes**: Check open issues
- ✨ **New Features**: From the roadmap or your ideas
- 📝 **Documentation**: Improve guides and comments
- 🌍 **Translations**: Add support for new languages
- 🎨 **UI/UX**: Design improvements and accessibility
- 🧪 **Tests**: Increase test coverage

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 ShopList Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## 👨‍💻 Author

<div align="center">

**Gundogar**

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/yourusername)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/yourprofile)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/yourhandle)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:your.email@example.com)

</div>

---

## 🙏 Acknowledgments

Special thanks to:

- 🎯 **JetBrains** - For Kotlin and Compose Multiplatform
- 🎨 **Google** - For Material Design and Android libraries
- 💾 **Cash App** - For SQLDelight ORM
- 🌟 **Kotlin Community** - For amazing support and resources
- 📚 **Open Source Contributors** - For inspiration and code samples

---

## 📊 Project Stats

<div align="center">

![Lines of Code](https://img.shields.io/tokei/lines/github/yourusername/shoplist?style=flat-square)
![GitHub code size](https://img.shields.io/github/languages/code-size/yourusername/shoplist?style=flat-square)
![GitHub repo size](https://img.shields.io/github/repo-size/yourusername/shoplist?style=flat-square)
![GitHub language count](https://img.shields.io/github/languages/count/yourusername/shoplist?style=flat-square)
![GitHub top language](https://img.shields.io/github/languages/top/yourusername/shoplist?style=flat-square&color=7F52FF)

</div>

---

## 🔗 Useful Resources

### Official Documentation

- 📘 [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- 🎨 [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- 🎯 [Material Design 3](https://m3.material.io/)
- 💾 [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)

### Project Documentation

- 🏗️ [MVVM Architecture Guide](./MVVM_ARCHITECTURE.md)
- 🛠️ [Development Guide](./CLAUDE.md)
- 📋 [Contributing Guidelines](#-contributing)

### Learning Resources

- 🎓 [Kotlin for Android Developers](https://kotlinlang.org/docs/android-overview.html)
- 🎯 [Jetpack Compose Pathway](https://developer.android.com/courses/pathways/compose)
- 🧪 [Testing in Compose](https://developer.android.com/jetpack/compose/testing)

---

## 📱 Download

<div align="center">

### Coming Soon to App Stores!

[![Google Play](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](#)
[![App Store](https://img.shields.io/badge/App_Store-0D96F6?style=for-the-badge&logo=app-store&logoColor=white)](#)

*Currently in development. Star the repo to get notified when it launches!*

</div>

---

## 💬 Support

Having issues or questions? We're here to help!

- 🐛 **Bug Reports**: [Open an issue](https://github.com/yourusername/shoplist/issues/new?template=bug_report.md)
- 💡 **Feature Requests**: [Request a feature](https://github.com/yourusername/shoplist/issues/new?template=feature_request.md)
- 💬 **Discussions**: [Join the discussion](https://github.com/yourusername/shoplist/discussions)
- 📧 **Email**: [your.email@example.com](mailto:your.email@example.com)

---

<div align="center">

### ⭐ If you like this project, give it a star!

**Built with 💚 using Kotlin Multiplatform**

<sub>ShopList © 2025 - Making shopping lists beautiful and accessible</sub>

[⬆ Back to Top](#-shoplist)

</div>
