package com.example.Arena.ui.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.Arena.db.room.Game
import com.example.Arena.di.GamesApi
import com.example.Arena.model.gamesmodel.GamesModel
import com.example.Arena.repository.GameRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@HiltViewModel
class GamesViewModel @Inject constructor(
    private val api: GamesApi,
    private val gameRepo: GameRepo,

    ) : ViewModel() {
    private val TAG = "GamesViewModel"

    private val searchGamesLiveData = MutableLiveData<GamesModel>()
    val _searcGamesLiveData: LiveData<GamesModel> = searchGamesLiveData

    suspend fun getSearchGameDetail(genre: String,exact:Boolean,precise:Boolean) {
        viewModelScope.launch {
            val data = api.searchOutGamesPaging(genre,exact=true,precise = true)
            searchGamesLiveData.value = data

        }
    }

    fun getGamePaginated(name: String): Flow<PagingData<Game>> {
        return gameRepo.getAllGamePagination(name)

    }



}
