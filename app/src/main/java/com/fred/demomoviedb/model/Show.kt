package com.fred.demomoviedb.model

import com.google.gson.annotations.SerializedName

data class Show(
    val id: Long,
    val name: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("first_air_date") val releaseDate: String,
    val popularity: Double
)