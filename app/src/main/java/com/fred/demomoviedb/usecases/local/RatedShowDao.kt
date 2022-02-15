package com.fred.demomoviedb.usecases.local

import androidx.room.*
import com.fred.demomoviedb.model.RatedShow

@Dao
interface RatedShowDao {

    @Query("SELECT * FROM RatedShow")
    suspend fun getAllRatedShows(): List<RatedShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatedShowsList(shows: List<RatedShow>)

    @Update
    suspend fun updateRatedShowsList(shows: List<RatedShow>)
}