package com.example.Arena.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyRemote(remoteKeys: List<RemoteKeys>)

    @Query("Select * From remote_keys where gamedId = :id")
    suspend fun getKeyRemote(id: String): RemoteKeys?


    @Query("DELETE FROM remote_keys")
    fun deleteAllGameKeyRemote()
}