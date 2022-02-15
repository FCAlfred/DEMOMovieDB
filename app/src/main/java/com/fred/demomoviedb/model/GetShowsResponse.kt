package com.fred.demomoviedb.model

import com.google.gson.annotations.SerializedName

data class GetShowsResponse(
    val page: Int,
    @SerializedName("results") val shows: List<Show>,
    @SerializedName("total_pages") val pages: Int
)
