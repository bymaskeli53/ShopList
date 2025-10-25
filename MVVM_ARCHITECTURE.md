# MVVM Architecture Documentation

## Overview

This document describes the MVVM (Model-View-ViewModel) architecture implemented in the ShopList application. The project follows clean architecture principles with clear separation of concerns.

## Architecture Layers

### 1. Domain Layer (`domain/`)
The domain layer contains business logic and domain models. It's independent of any framework or external dependencies.

#### **domain/model/**
- `ShoppingList.kt`: Domain model representing a shopping list
- `ShoppingItem.kt`: Domain model representing an item in a shopping list
- `ShoppingItemInput.kt`: Form input state for creating/editing items

#### **domain/repository/**
- `ShoppingRepository.kt`: Repository interface defining contracts for data operations
  - `getAllLists()`: Get all shopping lists as Flow
  - `getListById()`: Get a single list by ID
  - `createList()`: Create a new list
  - `updateList()`: Update existing list
  - `toggleListCompleted()`: Toggle completion status
  - `deleteList()`: Delete a list
  - `restoreList()`: Restore deleted list (undo)

### 2. Data Layer (`data/`)
The data layer implements the repository interface and handles data persistence.

#### **data/local/**
- `DatabaseDriverFactory.kt`: Platform-specific database driver factory (expect/actual)
  - Android: Uses AndroidSqliteDriver
  - iOS: Uses NativeSqliteDriver

#### **data/model/**
- `ShoppingListEntity.kt`: Data layer entity with mapper functions
  - `toDomain()`: Converts data entities to domain models
  - `toEntity()`: Converts domain models to data entities

#### **data/repository/**
- `ShoppingRepositoryImpl.kt`: Concrete implementation of ShoppingRepository
  - Uses SQLDelight for database operations
  - Handles all CRUD operations
  - Maps between data entities and domain models

### 3. Presentation Layer (`presentation/`)
The presentation layer contains UI components and ViewModels using Jetpack Compose.

#### **presentation/list/** - Shopping List Screen
- `ShoppingListScreen.kt`: Main screen displaying all shopping lists
  - Search functionality
  - Swipe-to-delete with undo
  - List count indicator
  - TTS (Text-to-Speech) support
- `ShoppingListViewModel.kt`: ViewModel managing list screen state
  - Exposes `lists` StateFlow
  - `toggleListCompleted()`
  - `deleteList()`
  - `restoreList()`
- `components/ListRow.kt`: Reusable component for displaying a single list

#### **presentation/add/** - Add Item Screen
- `AddItemScreen.kt`: Screen for creating new shopping lists
  - Dynamic item addition/deletion
  - Animated item cards
  - Form validation
- `AddItemViewModel.kt`: ViewModel for adding lists
  - `createList()`: Creates new list with items

#### **presentation/detail/** - Detail Screen
- `DetailScreen.kt`: Screen for editing existing lists
  - Edit list title
  - Add/remove items
  - TTS support
  - Animated item management
- `DetailViewModel.kt`: ViewModel for editing lists
  - `updateList()`: Saves changes

#### **presentation/theme/**
- `Color.kt`: Color scheme definitions (dark/light)
- `Theme.kt`: Main theme composable with dynamic color support (Material You on Android 12+)
- Platform-specific implementations:
  - `Theme.android.kt`: Android dynamic colors
  - `Theme.ios.kt`: iOS static colors

### 4. Utility Layer (`util/`)
Contains shared utilities and platform-specific implementations.

#### **util/tts/**
- `TextToSpeechManager.kt`: Common TTS interface (expect/actual)
  - `speak(text: String)`
  - `stop()`
  - `isSpeaking(): Boolean`
  - `shutdown()`
- Platform implementations:
  - `TextToSpeechManager.android.kt`: Android TTS
  - `TextToSpeechManager.ios.kt`: iOS AVSpeechSynthesizer

## Project Structure

```
com.gundogar.shoplist/
├── data/
│   ├── local/
│   │   └── DatabaseDriverFactory.kt
│   ├── model/
│   │   └── ShoppingListEntity.kt
│   └── repository/
│       └── ShoppingRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   ├── ShoppingList.kt
│   │   ├── ShoppingItem.kt
│   │   └── ShoppingItemInput.kt
│   └── repository/
│       └── ShoppingRepository.kt
├── presentation/
│   ├── list/
│   │   ├── ShoppingListScreen.kt
│   │   ├── ShoppingListViewModel.kt
│   │   └── components/
│   │       └── ListRow.kt
│   ├── add/
│   │   ├── AddItemScreen.kt
│   │   └── AddItemViewModel.kt
│   ├── detail/
│   │   ├── DetailScreen.kt
│   │   └── DetailViewModel.kt
│   └── theme/
│       ├── Color.kt
│       └── Theme.kt
├── util/
│   └── tts/
│       └── TextToSpeechManager.kt
└── App.kt
```

## Data Flow

1. **User Interaction** → UI Component (Screen)
2. **UI Component** → ViewModel (via callbacks)
3. **ViewModel** → Repository (via domain interface)
4. **Repository** → Database/Data Source
5. **Database** → Repository (data entities)
6. **Repository** → Domain Models (via mappers)
7. **Domain Models** → ViewModel (via Flow)
8. **ViewModel** → UI Component (via StateFlow)

## Key Design Patterns

### 1. **Repository Pattern**
- Domain layer defines the contract (`ShoppingRepository` interface)
- Data layer provides implementation (`ShoppingRepositoryImpl`)
- Allows easy testing and swapping of data sources

### 2. **Dependency Inversion**
- ViewModels depend on repository interface (domain layer)
- Implementation details are in data layer
- Domain layer has no dependencies on data or presentation layers

### 3. **Reactive Programming**
- Uses Kotlin Flow for reactive data streams
- ViewModels expose StateFlow for UI observation
- Automatic UI updates when data changes

### 4. **Mapper Pattern**
- Extension functions convert between layers
- `toDomain()`: Data entities → Domain models
- `toEntity()`: Domain models → Data entities

### 5. **Single Responsibility Principle**
- Each ViewModel manages one screen
- Repository handles only data operations
- Clear separation between layers

## Navigation

Navigation is handled in `App.kt` using Jetpack Compose Navigation:

```kotlin
NavHost(startDestination = "shopping_list") {
    composable("shopping_list") { ShoppingListScreen(...) }
    composable("add_item") { AddItemScreen(...) }
    composable("detail/{listId}") { DetailScreen(...) }
}
```

## State Management

- **ViewModels**: Hold UI state using StateFlow
- **Repository**: Provides data via Flow
- **Compose**: Observes state changes via `collectAsState()`

## Testing Strategy

The MVVM architecture makes testing easier:

1. **Unit Tests**
   - Test ViewModels with mock repositories
   - Test domain models
   - Test mappers

2. **Integration Tests**
   - Test repository implementations
   - Test database operations

3. **UI Tests**
   - Test screens with fake ViewModels
   - Test user interactions

## Benefits of This Architecture

1. **Separation of Concerns**: Each layer has a single responsibility
2. **Testability**: Easy to mock and test each layer independently
3. **Maintainability**: Changes in one layer don't affect others
4. **Scalability**: Easy to add new features
5. **Platform Independence**: Domain logic works across Android, iOS, Desktop
6. **Reactive UI**: Automatic UI updates with Flow/StateFlow
7. **Type Safety**: Strong typing across all layers

## Migration from Old Structure

The old flat structure has been migrated to MVVM:

**Before:**
- All files in root package
- `ShoppingViewModel.kt` handled all logic
- Direct database access from ViewModel
- UI models mixed with data models

**After:**
- Clear layer separation
- Dedicated ViewModels per screen
- Repository abstracts data access
- Separate domain and data models
- Mapper functions handle conversions

## Adding New Features

To add a new feature:

1. **Domain Layer**: Create domain models and update repository interface
2. **Data Layer**: Implement repository methods, create entities and mappers
3. **Presentation Layer**: Create screen, ViewModel, and components
4. **Navigation**: Add route in App.kt
5. **Wire Up**: Connect ViewModel to repository in App.kt

## Best Practices

1. **Never** pass ViewModels between screens
2. **Always** use domain models in ViewModels
3. **Keep** ViewModels lifecycle-aware
4. **Use** StateFlow for observable state
5. **Avoid** business logic in UI components
6. **Map** between layers at boundaries
7. **Keep** domain layer pure (no Android/iOS dependencies)

## Dependencies

- **SQLDelight**: Database operations
- **Kotlin Coroutines**: Asynchronous programming
- **Kotlin Flow**: Reactive streams
- **Jetpack Compose**: UI framework
- **Navigation Compose**: Navigation
- **ViewModel Compose**: ViewModel integration
- **Material3**: UI components and theming
