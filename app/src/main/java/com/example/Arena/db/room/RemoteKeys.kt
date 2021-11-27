package com.example.Arena.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
        @PrimaryKey(autoGenerate = false)
        val gamedId: String,
        val prevKey: Int?,
        val nextKey: Int?
)
