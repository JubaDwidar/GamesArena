package com.example.Arena.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.Arena.db.room.Dao
import com.example.Arena.db.room.Game
import com.example.Arena.db.RemoteMediatorClass
import com.example.Arena.db.room.RoomDb
import com.example.Arena.di.GamesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 40

@ExperimentalCoroutinesApi
class GameRepo @Inject constructor(
        private val api: GamesApi,
        private val roomDao: Dao,
        private val rom: RoomDb
) {
    @ExperimentalPagingApi
    fun getAllGamePagination(name: String): Flow<PagingData<Game>> {
        val pagingSouRceFactory = { roomDao.getAllGamesPaging() }

        return Pager(
                config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        maxSize = PAGE_SIZE * (PAGE_SIZE + 2),
                        enablePlaceholders = false,
                ),

                remoteMediator = RemoteMediatorClass(gamesApi = api,
                        roomDb = rom,
                        name = name),
                pagingSourceFactory = pagingSouRceFactory).flow

    }


}