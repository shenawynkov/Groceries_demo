# Groceries Demo - Shopping List App

A simple shopping list app built with Jetpack Compose and Material3.

## Features

- Add grocery items with a name and category (Milk, Vegetables, Fruits, Breads, Meats)
- Mark items as purchased (strikethrough + checkbox)
- Edit or delete items
- Filter the list by category
- Sort by name, category, or purchase status
- Data persists between app launches via Room

## Tech Stack

- Kotlin
- Jetpack Compose + Material3
- MVI architecture (single UiState, sealed Intent)
- Room for local persistence
- Hilt for dependency injection
- Coroutines / Flow

## Build & Run

1. Open the project in Android Studio (Ladybug or newer)
2. Sync Gradle
3. Run on an emulator or device (API 24+)

## Tests

Run unit tests from the terminal:

```
./gradlew test
```

Or right-click `GroceryListViewModelTest` in Android Studio and run.
