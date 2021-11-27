package com.example.Arena.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Arena.di.GamesApi
import com.example.Arena.model.gdetailsmodel.GameDetailsModel
import com.example.Arena.model.trailermodel.TrailerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesDetailViewModel @Inject constructor(
        private val api: GamesApi,
) : ViewModel() {

    private val gameData = MutableLiveData<GameDetailsModel>()
    val gameLiveData: LiveData<GameDetailsModel> = gameData


    private val videoGameData = MutableLiveData<TrailerModel>()
    val videoGameLiveData: LiveData<TrailerModel> = videoGameData


    suspend fun getGameDetail(id: String) {
        viewModelScope.launch {
            val data = api.getSpecificGame(id)
            gameData.value = data
        }
    }
    suspend fun getGameTrailer(slug: String) {
        viewModelScope.launch {
            val videoData = api.getSpecificGameTrailer(slug)
            videoGameData.value = videoData
        }
    }
}