package com.example.Arena.model.screenshotmodel


/**
 * model class that contain data set that receive rest api data from ScreenShot Type
 */
data class ScreenShotModel(
        var count: Int,
        var results: List<ScreenShot>

)