# OpenRide 

OpenRide is an Android application that bridges Google Maps and ride-sharing apps.

Instead of manually copying a destination from Google Maps and searching for it again inside a ride-hailing app, OpenRide lets users share a location directly from Google Maps and launch their preferred ride app with the destination already filled in.



## Supported Apps

| App | Status |
|------|---------|
| Uber | ✅ Supported |
| Rapido | ✅ Supported |
| Ola | ✅ Supported |
| Namma Yatri | 🚧 In Progress |



## How It Works

```text
Google Maps
      ↓
Share Location
      ↓
OpenRide
      ↓
Choose Ride App
      ↓
Launch Ride App
```



## Tech Stack

### Android
- Kotlin
- Jetpack Compose
- Material 3

### Android APIs
- Intents
- Share Intents
- PackageManager
- Package Visibility APIs

### Networking
- OkHttp



## Project Structure

```text
app/
├── MainActivity.kt
│
├── applauncher/
│   ├── UberLauncher.kt
│   ├── RapidoLauncher.kt
│   ├── OlaLauncher.kt
│   └── NammaYatriLauncher.kt
│
├── components/
│   └── LaunchButton.kt
│
├── rides/
│   ├── RideApp.kt
│   ├── RideAppDetector.kt
│   └── RideLauncher.kt
│
├── ui/
    ├── RideLocation.kt
    ├── LocationParser.kt
    └── UrlResolver.kt
```


## Roadmap

### Next

- Namma Yatri integration
- Improved error handling
- Better loading states

### Future
- Material 3 UI improvements
- Additional ride-hailing app integrations


