package com.example.Arena.model.gdetailsmodel



/**
 * model class that contain data set that receive rest api data from GameDetails Type
 */
data class GameDetailsModel(
        var id: Int,
        var name: String,
        var description: String,
        var background_image: String,
        var rating: Double,
        var released: String
)
