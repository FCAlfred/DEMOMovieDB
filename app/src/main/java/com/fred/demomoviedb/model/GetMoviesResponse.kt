package com.fred.demomoviedb.model

import com.google.gson.annotations.SerializedName

data class GetMoviesResponse(
    val page: Int,
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_pages") val pages: Int
)
