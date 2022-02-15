package com.fred.demomoviedb.usecases.local

import androidx.room.*
import com.fred.demomoviedb.model.RatedMovie

@Dao
interface RatedMovieDao {

    @Query("SELECT * FROM RatedMovie")
    suspend fun getAllRatedMovies(): List<RatedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatedMovieList(movies: List<RatedMovie>)

    @Update
    suspend fun updateRatedMovieList(movies: List<RatedMovie>)
}