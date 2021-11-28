# GamesArena

GamesArena is VideoGame Paltform that shows all Games based on its genre with alot of details about each game.


# Challenge description
Load movies genre.
Load movies list based on genre.
Ability to search movies by name.
Load more games with Pagination.

Show movies details with the following details :-

    Movie Title.
    Movie Year.
    Movie Rate.
    Movie Background.
    Movie Describtion.
    Movie Trailer (if any).

# Preview

https://user-images.githubusercontent.com/31703159/143766926-55600888-dd3c-4350-8d7f-aabb283d33e0.mp4

# Languages, libraries and tools used

    Kotlin
    androidX libraries
    Android LifeCycle
    Glide
    Room
    Android Pagination Library
    Retrofit2
    Hilt
    coroutines
   
# Implementation

    In this project I'm using MVVM Kotlin as an application architecture adopted from the architecture blueprints sample with these design patterns in mind:-

    Repository Pattern
    Facade Pattern
    Singleton
    Observables
    Adapters

    Using Hilt for dependency injection that will make testing easier and our make code cleaner and more readable.

    Using Retrofit library to handle the APIs stuff.

    Using Room for caching movies.

    Using Pagination Library to smooth the load of data in the inital and search state.

    Using Glode for great animation in loading Images and empty state design.
