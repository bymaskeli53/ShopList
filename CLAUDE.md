# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ShopList is a Kotlin Multiplatform shopping list application built with Compose Multiplatform. It targets Android, iOS, and Desktop (JVM) platforms with shared business logic and UI.

## Build Commands

```bash
# Build the project
./gradlew build

# Run Android app
./gradlew :composeApp:installDebug

# Run Desktop (JVM) app
./gradlew :composeApp:run

# Generate SQLDelight code (after schema changes)
./gradlew generateCommonMainShopListDatabaseInterface

# Clean build
./gradlew clean
```

## Architecture

### Data Layer (Repository Pattern)

The app uses a repository pattern with SQLDelight for database operations:

- **Database Schema**: `composeApp/src/commonMain/sqldelight/com/gundogar/shoplist/database/ShoppingItem.sq`
  - `ShoppingList` table: Stores list metadata (id, title, bought, timestamps)
  - `ShoppingListItem` table: Stores individual items with foreign key to list

- **Repository**: `ShoppingRepository.kt`
  - Provides Flow-based reactive data access
  - All database operations are suspend functions running on `Dispatchers.IO`
  - Returns `ShoppingListWithItems` data class combining list + items

- **Platform Drivers**: SQLDelight uses platform-specific drivers:
  - Android: `DatabaseDriverFactory.android.kt`
  - iOS: `DatabaseDriverFactory.ios.kt`
  - JVM: `DatabaseDriverFactory.jvm.kt`

### UI Layer (MVVM)

- **ViewModel**: `ShoppingViewModel.kt`
  - Single source of truth exposing `StateFlow<List<ShoppingListUI>>`
  - Handles all business logic and repository interactions
  - Uses `viewModelScope` for coroutines

- **Screens**:
  - `ShoppingListScreen.kt`: Main list view with swipe-to-delete and undo
  - `DetailScreen.kt`: Edit existing list (title + items)
  - `AddItemScreen.kt`: Create new list (title + items)
  - `ItemRow.kt`: Reusable list item component

- **Navigation**: Uses Jetpack Compose Navigation with routes:
  - `shopping_list` → Main screen
  - `add_item` → Create new list
  - `detail/{listId}` → Edit list

### Theming System

The app supports **Material You dynamic colors** on Android 12+:

- **Theme files**: `composeApp/src/commonMain/kotlin/com/gundogar/shoplist/ui/theme/`
  - `Theme.kt`: Common theme with `expect`/`actual` for dynamic colors
  - `Theme.android.kt`: Dynamic color implementation using `dynamicDarkColorScheme`/`dynamicLightColorScheme`
  - `Theme.ios.kt`: Falls back to static color schemes
  - `Color.kt`: Defines `DarkColorScheme` and `LightColorScheme`

- **Usage**: All screens must use `ShopListTheme` wrapper and access colors via `MaterialTheme.colorScheme.*`

### Text-to-Speech (Platform-Specific)

- **Common Interface**: `composeApp/src/commonMain/kotlin/com/gundogar/shoplist/tts/TextToSpeechManager.kt`
- **Platform Implementations**:
  - Android: Uses `android.speech.tts.TextToSpeech`
  - iOS: Uses `AVSpeechSynthesizer`
- **Pattern**: `expect`/`actual` for platform-specific code

## Key Conventions

### Database Changes

When modifying the database schema:

1. Update `ShoppingItem.sq` (add columns, queries)
2. Run `./gradlew generateCommonMainShopListDatabaseInterface`
3. Update `ShoppingRepository.kt` data classes and methods
4. Update UI models (`ShoppingListUI`, `ShoppingListItemUI`)
5. Update ViewModel mapping in `loadLists()`
6. Update all affected UI screens

**Important**: SQLDelight queries are order-sensitive. When adding columns to INSERT/UPDATE, ensure the order matches the table definition.

### UI State Management

- All screens receive **immutable state** and **callbacks** as parameters
- State is hoisted to `ShoppingViewModel` and passed down via `App.kt`
- Use `remember(key)` to reset state when navigating between lists
- Suspend operations (like save) should use `viewModelScope.launch` or be marked `suspend`

### Multiplatform Code Organization

```
composeApp/src/
├── commonMain/       # Shared code for all platforms
│   ├── kotlin/       # Common Kotlin code
│   └── sqldelight/   # Database schema
├── androidMain/      # Android-specific implementations
├── iosMain/          # iOS-specific implementations
└── jvmMain/          # Desktop-specific implementations
```

Use `expect`/`actual` declarations for platform-specific APIs (TTS, dynamic colors, database drivers).

### Material Design 3

- All screens use Material3 components (`Scaffold`, `TopAppBar`, `FloatingActionButton`, etc.)
- Colors come from `MaterialTheme.colorScheme.*` (never hardcoded)
- Text styles from `MaterialTheme.typography.*`
- Minimum touch targets: 48dp for interactive elements

## Database Schema Details

The `ShoppingList.title` field is optional (default empty string). When displayed:
- If title is present: Show title in bold with item preview
- If title is empty: Show items as comma-separated list (fallback)

Lists support:
- Swipe-to-delete with undo (using `SnackbarResult`)
- Toggle bought status
- Text-to-speech reading of items

## Version Information

- Kotlin: 2.2.0
- Compose Multiplatform: 1.8.2
- SQLDelight: 2.0.2
- Target platforms: Android (minSdk 24), iOS, Desktop (JVM)
- Material Design: 3
