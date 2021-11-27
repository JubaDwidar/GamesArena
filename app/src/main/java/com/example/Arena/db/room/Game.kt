package com.example.Arena.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
        val id: Int,
        @PrimaryKey
        val name: String,
        val slug: String,
        val rating: Double,
        val background_image: String

)
