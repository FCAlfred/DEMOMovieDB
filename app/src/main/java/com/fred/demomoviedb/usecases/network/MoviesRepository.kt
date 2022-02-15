package com.fred.demomoviedb.usecases.network

import com.fred.demomoviedb.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
    private val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)
    }

    fun getPopularMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getPopularMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTopRatedMovies(
        page: Int = 1,
        onSuccess: (movies: List<RatedMovie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getTopRatedMovies(page = page)
            .enqueue(object : Callback<GetRatedMoviesResponse> {
                override fun onResponse(
                    call: Call<GetRatedMoviesResponse>,
                    response: Response<GetRatedMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetRatedMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getRecommendationMovies(
        movieId: Long,
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getRecommendationMovies(movieId = movieId, page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getPopularShows(
        page: Int = 1,
        onSuccess: (shows: List<Show>) -> Unit,
        onError: () -> Unit
    ) {
        api.getPopularShows(page = page)
            .enqueue(object : Callback<GetShowsResponse> {
                override fun onResponse(
                    call: Call<GetShowsResponse>,
                    response: Response<GetShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.shows)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetShowsResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getRatedShows(
        page: Int = 1,
        onSuccess: (showList: List<RatedShow>) -> Unit,
        onError: () -> Unit
    ) {
        api.getRatedShows(page = page)
            .enqueue(object : Callback<GetRatedShowsResponse> {
                override fun onResponse(
                    call: Call<GetRatedShowsResponse>,
                    response: Response<GetRatedShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.shows)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetRatedShowsResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getRecommendationShow(
        showId: Long,
        page: Int = 1,
        onSuccess: (showList: List<Show>) -> Unit,
        onError: () -> Unit
    ) {
        api.getRecommendationShow(tvId = showId, page = page)
            .enqueue(object : Callback<GetShowsResponse> {
                override fun onResponse(
                    call: Call<GetShowsResponse>,
                    response: Response<GetShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.shows)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetShowsResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getRecommendationRatedShow(
        showId: Long,
        page: Int = 1,
        onSuccess: (showList: List<RatedShow>) -> Unit,
        onError: () -> Unit
    ) {
        api.getRecommendationRatedShow(tvId = showId, page = page)
            .enqueue(object : Callback<GetRatedShowsResponse> {
                override fun onResponse(
                    call: Call<GetRatedShowsResponse>,
                    response: Response<GetRatedShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.shows)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetRatedShowsResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }
}