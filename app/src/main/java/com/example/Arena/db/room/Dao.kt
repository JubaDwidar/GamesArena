package com.example.Arena.db.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: List<Game>)

    @Query("Select * From game ")
    fun getAllGames(): Flow<List<Game>>


    @Query("DELETE FROM game")
    fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGamePaging(game: List<Game>)

    @Query("Select * FROM game ")
    fun getAllGamesPaging(): PagingSource<Int, Game>



    @Query("DELETE FROM game")
    suspend fun deleteAllGamesPaging()

}