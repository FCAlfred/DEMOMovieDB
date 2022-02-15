# DEMOMovieDB

![movie](https://user-images.githubusercontent.com/41550868/153776191-6e229fde-7b9d-43cf-822d-69110577756d.png)

----------------------------------------------------------------

DEMOMovieDB is a gorgeous client application for [TMDb](https://www.themoviedb.org) on Android, built using Kotlin.


## Architecture and Tech-stack

* Built on MVVM architecture pattern
* Uses [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/), specifically Fragments, ViewModel, LiveData, viewBinding, Paging, viewPager2, RecyclerViews and Tabs.
* Animations with [Lottie](https://github.com/airbnb/lottie-ios) from Airbnb.
* Uses [Retrofit](https://square.github.io/retrofit/) for making API calls.
* Uses [Glide](https://github.com/bumptech/glide) for image loading.
* Uses [Shimmer](https://facebook.github.io/shimmer-android/) for loading visual effect.
* Uses [Toasty](https://github.com/GrenderG/Toasty) for custom Toast messages.
* Uses [ROOM](https://github.com/androidx-releases/Room/releases) for local storage.
* Main Content was Built on a Single-Activity Architecture. Every screen in the app is a fragment(except Intro).

## Features
* Discover Top Rated and Popular movies/tvShows on TMDb.
* See Recommendations after select a movie/tvShow from Popular or Rated.
* View movie/tvShow details like release date, rating, overview, pictures and more, right inside the app.
* With validation if you don't have Internet connection. You Have a button to reconnect after turn on Wi-Fi.

## Screenshots

![RoomDb](https://user-images.githubusercontent.com/41550868/154133506-f9a64095-00fa-4e2d-8374-4af324cb7b95.gif)


## Planned Features
* Implementation of Dagger/Hilt (dependency injection).
* Implementation of a new function to be able to see Trailer movies/tvShows.

