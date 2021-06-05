# TmDB
Application built with Jetpack Compose which uses TmDB as data source.

To get this app running add your API KEY to `local.properties` as shown below, if you don't have one you can easily get one from [here](https://www.themoviedb.org/settings/api) after creating a free account.
```
api="<your-api-key>"
```

SDK levels supported 
--------------

- Minimum SDK 23
- Target SDK 30

Built With 🛠
--------------

- [Jetpack Compose](https://developer.android.com/jetpack/compose/) - Jetpack Compose is Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
- [Kotlin](https://kotlinlang.org/) - Officially supported programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
- [Navigation](https://developer.android.com/jetpack/compose/navigation) - For naviation between composables.
- [Hilt](https://dagger.dev/hilt/) - Dependency Injection Framework
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Accompanist](https://google.github.io/accompanist/) - Accompanist is a group of libraries that contains some utilities which I've found myself copying around projects which use Jetpack Compose.
- [Moshi](https://github.com/square/moshi) - Moshi is a modern JSON library for Android and Java, to ease the process of parsing JSON.
- [Retrofit](https://github.com/square/retrofit) - A type-safe HTTP client for Android and Java.
- [Store](https://github.com/dropbox/Store) - Store is a Kotlin library for loading data from remote and local sources.

Package structure
-----------------
```
app/src/main/java/dev/shreyansh/tmdb
├── data                              # Data layer
│   ├── api                           # API service
│   ├── db                            # Database setup
│   │   └── dao                       # Model DAO's
│   ├── model                         # Models
│   │   └── responses                 # Wrapper classes for models to parse API respose
│   └── repository                    # Repository classes which acts as a single source of truth for the ViewModel
├── di                                # Dependency Injection setup
├── ui                                # UI layer
│   ├── about                         # UI for about screen
│   ├── home                          # UI for home screen
│   ├── movie                         # UI for movie screen
│   ├── navigation                    # Navigation setup
│   ├── theme                         # Theme for TmDB
│   └── tvShows                       # UI for Tv show screen
└── utils                             # Helper functions and classes
```

Miscellaneous Information
----------------

This app is written while keeping in mind the [MVVM architecture](https://developer.android.com/jetpack/guide).
