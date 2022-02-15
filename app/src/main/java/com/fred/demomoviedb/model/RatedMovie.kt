package com.fred.demomoviedb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RatedMovie")
data class RatedMovie(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "posterPath") @SerializedName("poster_path") val posterPath: String?,
    @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") val rating: Float,
    @ColumnInfo(name = "release_date") @SerializedName("release_date") val releaseDate: String,
    val popularity: Double
)
