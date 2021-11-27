package com.example.Arena.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.Arena.db.room.Game
import com.example.Arena.db.room.RemoteKeys
import com.example.Arena.db.room.RoomDb
import com.example.Arena.di.GamesApi
import retrofit2.HttpException
import java.io.IOException

/**
 * RemoteMediatorClass that handle fetching data from remote api
 * save in local database
 * display it in GamesActivity

 */

const val StTARTING_PAGING_INDEX = 1

@ExperimentalPagingApi
class RemoteMediatorClass(
        private val gamesApi: GamesApi,
        private val roomDb: RoomDb,
        private val name: String
) : RemoteMediator<Int, Game>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    /**
     * load fun responsible for load data from api and caching in specific order based on its key value
     *
     */
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Game>): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = gamesApi.getAllGamesPaging(page = page, pageSize = state.config.pageSize, name)
            val isEndOfList = response.results.isEmpty()
            roomDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    roomDb.games().deleteAllGamesPaging()
                    roomDb.gameRemote().deleteAllGameKeyRemote()
                }
                val prevKey = if (page == StTARTING_PAGING_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKeys(it.name, prevKey, nextKey)

                }

                val myGames = response.results.map {
                    Game(it.id, it.name, it.slug, it.rating, it.background_image)
                }
                roomDb.gameRemote().insertKeyRemote(keys)
                roomDb.games().insertGamePaging(myGames)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }


    }


    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Game>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: StTARTING_PAGING_INDEX
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                val nextKey = remoteKey?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(state)
                val prevKey = remoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
                prevKey

            }
        }
    }

    /**
     * getRemoteKeyClosestToCurrentPosition fun get the current position
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Game>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { remoteName ->
                roomDb.gameRemote().getKeyRemote(remoteName)
            }
        }
    }


    /**
     * getLastRemoteKey fun get the last position on recycler view
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Game>): RemoteKeys? {
        return state.pages
                .lastOrNull {
                    it.data.isNotEmpty()
                }
                ?.data?.lastOrNull()?.let { game ->
                    roomDb.gameRemote().getKeyRemote(game.name)
                }

    }

    /**
     * getFirstRemoteKey fun get the first position on recycler view
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Game>): RemoteKeys? {
        return state.pages
                .firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let { game ->
                    roomDb.gameRemote().getKeyRemote(game.name)
                }
    }
}