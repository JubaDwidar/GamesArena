package com.example.Arena.model.gamesmodel

/**
 * model class that contain data set that receive rest api data from Game Type
 */
data class GamesModel(

        var count: Int,
        var results: List<Games>

)