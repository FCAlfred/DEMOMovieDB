package com.fred.demomoviedb.usecases.local

import androidx.room.*
import com.fred.demomoviedb.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM PopularMovies")
    suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<Movie>)

    @Update
    suspend fun updateMovieList(movies: List<Movie>)
}