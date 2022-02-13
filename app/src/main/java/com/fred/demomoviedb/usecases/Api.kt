package com.fred.demomoviedb.usecases

import com.fred.demomoviedb.model.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendationMovies(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("tv/popular")
    fun getPopularShows(
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("tv/top_rated")
    fun getRatedShows(
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("tv/{tv_id}/recommendations")
    fun getRecommendationShow(
        @Path("tv_id") tvId: Long,
        @Query("api_key") apiKey: String = "6288303bc29b854da7570e8cd5f1ecc0",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}