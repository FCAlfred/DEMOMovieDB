package com.fred.demomoviedb.model

import com.google.gson.annotations.SerializedName

data class GetRatedMoviesResponse(
    val page: Int,
    @SerializedName("results") val movies: List<RatedMovie>,
    @SerializedName("total_pages") val pages: Int
)
