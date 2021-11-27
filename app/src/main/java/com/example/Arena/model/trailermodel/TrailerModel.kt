package com.example.Arena.model.trailermodel



/**
 * model class that contain data set that receive rest api data from Trailer Type
 */
data class TrailerModel(
        val id: Int,
        val results: List<Trailer>

)