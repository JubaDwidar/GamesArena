package com.example.Arena.db.room


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Game::class, RemoteKeys::class], version = 4, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun games(): Dao
    abstract fun gameRemote(): RemoteKeyDAO
}