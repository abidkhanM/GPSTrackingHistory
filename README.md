# GPS Tracking

Android app for real-time GPS tracking, trip history and settings management, built with **Clean Architecture**, **Jetpack Compose** and Google's latest recommended Android practices.

---

## Features

- Real-time GPS tracking with live map view (Google Maps Compose)
- Pause / Resume / Stop trip control
- Trip history with start time, duration and distance
- Configurable location update interval and background tracking toggle
- Material 3 UI with dynamic color support and edge-to-edge display

---

## Architecture

This project follows **Clean Architecture** with three strict layers:

```
domain/          ← Pure Kotlin, zero Android dependencies
  model/         Domain models: Trip, LocationPoint, Settings
  repository/    TrackingRepository interface
  usecase/       One use case per operation (7 total)
  util/          Pure utility functions (haversine distance)

data/            ← Implements domain interfaces
  local/db/      Room entities, DAO
  local/datastore/ DataStore → emits domain Settings model
  mapper/        Entity ↔ domain model mappers
  repository/    TrackingRepositoryImpl

ui/              ← Only depends on domain models
  screens/
    tracking/    TrackingScreen + TrackingViewModel
    history/     TripHistoryScreen + TripHistoryViewModel
    settings/    SettingsScreen + SettingsViewModel
    airbnb/      AirBnbScreen (UI showcase)
  theme/         Material 3 theme
  util/          Shared UI utilities (formatElapsed)
```

Each layer only depends inward, UI depends on domain, data depends on domain, domain depends on nothing.

---

## Tech Stack

| Area | Library / Tool |
|---|---|
| Language | Kotlin 2.0.21 (100%) |
| UI | Jetpack Compose + Material 3 |
| Maps | Google Maps Compose 4.4.1 |
| Navigation | Navigation Compose 2.8.4 (type-safe routes) |
| DI | Hilt 2.51.1 |
| Build | KSP (replaces KAPT) |
| Database | Room 2.6.1 |
| Preferences | DataStore 1.1.1 |
| Async | Kotlin Coroutines + Flow |
| Location | Play Services Location 21.3.0 |

---

## Key Practices

- **Hilt** for compile-time verified dependency injection
- **KSP** instead of KAPT, faster builds, Kotlin-first
- **Type-safe Navigation 2.8+** with `@Serializable data object` routes
- **`collectAsStateWithLifecycle`** lifecycle-aware state collection, no wasted work in background
- **`stateIn(WhileSubscribed(5_000))`**  idiomatic Flow-to-StateFlow conversion in ViewModels
- **Single `UiState` data class** per ViewModel for clean, testable state
- **Location managed entirely in ViewModel** via `callbackFlow` — never in Composables
- **`enableEdgeToEdge()`** modern edge-to-edge display (replaces deprecated `window.statusBarColor`)
- **Room `exportSchema = true`** with schema directory, enables migration history tracking
- **`@Index` on foreign key columns** in Room, faster location point queries per trip
- **ViewModels co-located with their screens** feature-first folder structure

---

## Setup & Running

### 1. Clone the repository
```sh
git clone <your-repo-url>
```

### 2. Add your Google Maps API Key
Create or open `local.properties` in the project root and add:
```
MAPS_API_KEY=your_actual_api_key_here
```
[Get an API key](https://developers.google.com/maps/documentation/android-sdk/get-api-key) — billing must be enabled on your Google Cloud project.

### 3. Build and run
- Open in Android Studio Ladybug or later
- Sync Gradle (requires internet for dependency download)
- Run on a physical device or emulator with Google Play Services

---

## Project Structure

```
app/src/main/java/com/artificient/gpstracking/
├── App.kt                      # @HiltAndroidApp
├── MainActivity.kt             # @AndroidEntryPoint, enableEdgeToEdge
├── data/
│   ├── local/db/               # Room: entities, DAO, AppDatabase
│   ├── local/datastore/        # DataStore preferences
│   ├── mapper/                 # EntityMapper (entity ↔ domain)
│   └── repository/             # TrackingRepositoryImpl
├── di/
│   ├── DatabaseModule.kt       # Hilt: DB, DAO, DataStore providers
│   └── RepositoryModule.kt     # Hilt: binds interface to impl
├── domain/
│   ├── model/                  # Trip, LocationPoint, Settings
│   ├── repository/             # TrackingRepository (interface)
│   ├── usecase/                # GetAllTrips, StartTrip, StopTrip, ...
│   └── util/                   # haversineDistance()
├── location/
│   └── LocationDataSource.kt   # callbackFlow wrapping FusedLocationClient
├── navigation/
│   └── Navigation.kt           # Type-safe routes, MainNavigation, AppBottomBar
└── ui/
    ├── screens/
    │   ├── tracking/           # TrackingScreen + TrackingViewModel
    │   ├── history/            # TripHistoryScreen + TripHistoryViewModel
    │   ├── settings/           # SettingsScreen + SettingsViewModel
    │   └── airbnb/             # AirBnbScreen (UI showcase)
    ├── theme/                  # Color, Theme, Type
    └── util/                   # FormatUtils (formatElapsed)
```

---

## Permissions

```xml
ACCESS_FINE_LOCATION     <!-- Required for GPS tracking -->
ACCESS_COARSE_LOCATION   <!-- Fallback location -->
ACCESS_BACKGROUND_LOCATION <!-- For background tracking (API 29+) -->
```

Permission rationale dialogs are handled in `TrackingScreen` with proper rationale and settings redirect flows.

---

## Notes

- All sensitive keys are excluded from version control via `local.properties`
- Room schema files are committed to `app/schemas/` to track migration history
- Background tracking toggle is persisted in DataStore but a foreground service is not yet implemented — this is a planned enhancement

---

## License

MIT License

Copyright (c) 2026 Abid Khan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
