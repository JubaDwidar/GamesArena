package com.example.Arena.ui.genres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Arena.di.GamesApi
import com.example.Arena.model.genresmodel.GenresModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(gamesApi: GamesApi
) : ViewModel() {


    private val genreLiveData = MutableLiveData<GenresModel>()
    val _genreLiveData: LiveData<GenresModel> = genreLiveData

    init {
        viewModelScope.launch {
            val lastData = gamesApi.getGenres()
            genreLiveData.value = lastData

        }
    }


}