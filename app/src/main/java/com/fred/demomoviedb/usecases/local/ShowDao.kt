package com.fred.demomoviedb.usecases.local

import androidx.room.*
import com.fred.demomoviedb.model.Show

@Dao
interface ShowDao {

    @Query("SELECT * FROM PopularShows")
    suspend fun getAllShows(): List<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShowList(shows: List<Show>)

    @Update
    suspend fun updateShowList(shows: List<Show>)
}