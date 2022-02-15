package com.fred.demomoviedb.model

import com.google.gson.annotations.SerializedName

data class GetRatedShowsResponse(
    val page: Int,
    @SerializedName("results") val shows: List<RatedShow>,
    @SerializedName("total_pages") val pages: Int
)