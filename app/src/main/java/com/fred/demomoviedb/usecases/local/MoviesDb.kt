package com.fred.demomoviedb.usecases.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.model.RatedMovie
import com.fred.demomoviedb.model.RatedShow
import com.fred.demomoviedb.model.Show

@Database(
    entities = [Movie::class, Show::class, RatedMovie::class, RatedShow::class],
    version = 1
)
abstract class MoviesDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun showDao(): ShowDao

    abstract fun ratedMovieDao(): RatedMovieDao

    abstract fun ratedShowDao(): RatedShowDao

}