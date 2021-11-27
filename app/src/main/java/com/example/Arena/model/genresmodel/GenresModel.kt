package com.example.Arena.model.genresmodel


/**
 * model class that contain data set that receive rest api data from Genres Type
 */
data class GenresModel(

        var count: Int,
        var results: List<Genres>

)