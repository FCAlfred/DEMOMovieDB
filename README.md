# DEMOMovieDB

![movie](https://user-images.githubusercontent.com/41550868/153776191-6e229fde-7b9d-43cf-822d-69110577756d.png)

----------------------------------------------------------------

DEMOMovieDB is a gorgeous client application for [TMDb](https://www.themoviedb.org) on Android, built using Kotlin.


## Architecture and Tech-stack

* Built on MVVM architecture pattern
* Uses [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/), specifically Fragments, ViewModel, LiveData, viewBinding, Paging, viewPager2, RecyclerViews and Tabs.
* Animations with [Lottie](https://github.com/airbnb/lottie-ios) from Airbnb
* Uses [Retrofit](https://square.github.io/retrofit/) for making API calls.
* Uses [Glide](https://github.com/bumptech/glide) for image loading.
* Uses [Shimmer](https://facebook.github.io/shimmer-android/) for loading visual effect.
* Uses [Toasty](https://github.com/GrenderG/Toasty) for custom Toast messages.
* Main Content was Built on a Single-Activity Architecture. Every screen in the app is a fragment(except Intro).

## Features
* Discover Top Rated and Popular movies/tvShows on TMDb.
* See Recommendations after select a movie/tvShow from Popular or Rated.
* View movie/tvShow details like release date, rating, overview, pictures and more, right inside the app.
* With validation if you don't have Internet connection. You Have a button to reconnect after turn on Wi-Fi.

## Screenshots
![noConnectionApp](https://user-images.githubusercontent.com/41550868/153778821-d93980dc-bf22-4e63-bb5b-dfe7c60cd0ff.gif)

![mbdApp](https://user-images.githubusercontent.com/41550868/153778822-1f37e7dc-6c01-4af9-886c-50e58f1476c5.gif)

## Planned Features
* Implementation of ROOM Database to show content offline.
* Implementation of Dagger/Hilt (dependency injection).
* Implementation of a new function to be able to see Trailer movies/tvShows.

